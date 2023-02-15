import csv
import datetime
import json
import random
import re
from datetime import timedelta

import pandas as pd
from numpy import average
from pandas import array
from sklearn.impute import SimpleImputer
import pymongo
from neo4j import GraphDatabase

def random_date(start, end):
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = random.randrange(int_delta)
    return start + timedelta(seconds=random_second)

def cleaning_accommodations(dataframe):
    print("Cleaning...")

    dataframe = dataframe[["id", "name", "neighbourhood_cleansed", "beds", "bedrooms", "price",
                           "number_of_reviews", "property_type", "amenities"]]

    dataframe["reservations"] = None

    dataframe = dataframe[dataframe.property_type.isin(['Entire apartment', 'Private room in apartment',
                                                        'Private room in house', 'Private room in townhouse',
                                                        'Entire condominium', 'Entire house', 'Entire loft',
                                                        'Entire townhouse', 'Entire rental unit'])]

    dataframe = dataframe.drop(dataframe[dataframe.bedrooms.isnull()].index)
    dataframe["bedrooms"] = dataframe["bedrooms"].astype('int')

    #dataframe['property_type'] = dataframe.property_type.apply(lambda c: re.sub(' ', '_', c))
    dataframe["neighbourhood_cleansed"] = dataframe["neighbourhood_cleansed"].apply(
        lambda c: re.sub('[\s][-][\s][\S]+', '', c))
    dataframe["neighbourhood_cleansed"] = dataframe["neighbourhood_cleansed"].apply(
        lambda c: re.sub("[']", '', c)).astype('category')

    dataframe['price'] = dataframe.price.apply(lambda c: re.sub('[$,]', '', c)).astype('float')

    # nell'attributo beds completo le celle vuote con la media e casto ad int
    dataframe["beds"] = SimpleImputer(strategy='median').fit_transform(dataframe[["beds"]]).round()
    dataframe["beds"] = dataframe["beds"].astype('int')

    # inserisco la media nelle celle vuote dell'attributo review_score_rating
    dataframe["review_scores_value"] = 0
    dataframe["number_of_reviews"] = 0
    dataframe["images_URL"] = None
    dataframe["amenities"] = dataframe["amenities"].apply(lambda c: re.sub("\"", "", c))
    dataframe['amenities'] = dataframe['amenities'].apply(lambda x: x[1:-1])
    dataframe['amenities'] = dataframe['amenities'].apply(
        lambda x: re.sub(r'(?<=[a-zA-Z0-9])[,](?=[a-zA-Z0-9])', ' ', x))
    dataframe['amenities'] = dataframe['amenities'].apply(lambda x: re.sub(r'\\u[0-9]+', '', x))
    dataframe['amenities'] = dataframe['amenities'].apply(lambda x: re.sub(r'\'', '', x))
    for k in range(0, len(dataframe)):
        dataframe["id"].iloc[k] = k
        dataframe["amenities"].iloc[k] = list(dataframe["amenities"].iloc[k].split(",")[0:10])
        dataframe["reservations"].iloc[k] = []
    dataframe.columns = [
        ["id", "name", "neighborhood", "num_beds", "num_rooms", "price", "num_reviews", "property_type", "amenities",
         "reservations", "rating", "images_URL"]]


    return dataframe


def accommodations_to_mongo():
    accommodations = pd.read_csv("dataset/cleaned/accommodations.csv")

    myclient = pymongo.MongoClient("mongodb://172.16.5.38:27017,172.16.5.39:27017,172.16.5.40:27017/")
    mydb = myclient["NYSleep"]
    mycol = mydb["accommodations"]

    for i in range(0, len(accommodations)):
        acc = accommodations.loc[i]
        amenities = (acc["amenities"].split(sep=","))
        for j in range(0, len(amenities)):
            amenities[j] = re.sub("\'|\[|\]", "", amenities[j])

        renter_string = accommodations.loc[i]["renter"]
        renter_string = re.sub('[\' }{ ]', '', renter_string)
        renter_string = re.sub(',', ':', renter_string).split(sep=":")
        renter_dict = {"id": int(renter_string[1]), "first_name": renter_string[3], "last_name": renter_string[5],
                       "work_email": renter_string[7], "phone": renter_string[9]}

        #start_date = random_date(datetime.datetime(2023, 3, 1), datetime.datetime(2023, 12, 1))
        #end_date = start_date + datetime.timedelta(days=10)
        #reservations_dict = [{"start_date": start_date, "end_date": end_date}]

        object_dict = {"_id": int(acc["id"]), "name": acc["name"], "neighborhood": acc["neighborhood"],
               "num_beds": int(acc["num_beds"]), "num_rooms": int(acc["num_rooms"]),
               "price": float(acc["price"]),
               "num_reviews": int(acc["num_reviews"]), "property_type": acc["property_type"],
               "amenities": amenities, "reservations": [],
               "rating": round(float(acc["rating"]), 1), "images_URL": None, "renter": renter_dict}
        mycol.insert_one(object_dict)




def avg_count_ratings():
    accommodations = pd.read_csv("dataset/cleaned/accommodations.csv")
    reviews = pd.read_csv("dataset/cleaned/cleaned_reviews.csv")

    for i in range(0, len(accommodations)):
        acc_reviews = reviews[reviews["id_acc"] == accommodations.id.loc[i]]
        avg_rate = average(acc_reviews["rate"])
        accommodations.rating.loc[i] = avg_rate
        accommodations.num_reviews.loc[i] = len(acc_reviews)
    return accommodations

def accommodations_to_neo4j():
    accommodations = pd.read_csv("dataset/cleaned/accommodations.csv")
    accommodations_to_neo4j = pd.DataFrame(columns=["id", "name", "neighborhood", "rating", "price"])
    for i in range (0, len(accommodations)):
        acc = accommodations.loc[i]
        row = [acc["id"], acc["name"], acc["neighborhood"], acc["rating"], acc["price"]]
        accommodations_to_neo4j.loc[i] = row
    accommodations_to_neo4j.to_csv("dataset/neo4j/accommodations_neo4j.csv", index=False)

def insert_accs_into_neo4j():
    URI = "bolt://172.16.5.40:7687"
    AUTH = ("neo4j", "NY_Sleep")
    accommodations = pd.read_csv("dataset/neo4j/accommodations_neo4j.csv")
    for i in range(0, len(accommodations)):
        acc = accommodations.loc[i]
        with GraphDatabase.driver(URI, auth=AUTH) as driver:
            with driver.session() as session:
                session.execute_write(create_acc, acc["id"], acc["name"], acc["neighborhood"], acc["price"], acc["rating"])
                print("Inserito")


def create_acc(tx, id, name, neighborhood, price, rating):
    tx.run("CREATE (a:accommodation {id: $id, name: $name, neighborhood: $neighborhood, price: $price, rating: $rating })",
           id=id, name=name, neighborhood=neighborhood, price=price, rating=rating)


def add_renter():
    property = pd.read_csv("dataset/neo4j/property_neo4j.csv")
    renters = pd.read_csv("dataset/cleaned/users.csv")
    accommodations = pd.read_csv("dataset/cleaned/accommodations.csv")
    accommodations["renter"] = ""

    for i in range (0, len(accommodations)):
        acc = accommodations.loc[i]
        renter_id = property.loc[i]["id_renter"]
        renter = renters[renters["id"] == renter_id]
        renter_dict = {"id" : renter_id, "first_name" : renter["first_name"].iloc[0],
                       "last_name": renter["last_name"].iloc[0], "work_email": renter["work_email"].iloc[0],
                       "phone": (renter["phone"].iloc[0]).strip()}
        acc["renter"] = str(renter_dict)
        accommodations.loc[i] = acc
    accommodations.to_csv("accommodations.csv", index=False)

def insert_property_into_neo4j():
    URI = "bolt://172.16.5.40:7687"
    AUTH = ("neo4j", "NY_Sleep")
    property = pd.read_csv("dataset/neo4j/property_neo4j.csv")
    for i in range(0, len(property)):
        prop = property.loc[i]
        with GraphDatabase.driver(URI, auth=AUTH) as driver:
            with driver.session() as session:
                session.execute_write(create_property, prop["id_renter"], prop["id_acc"])
                print("Inserito " + str(i))

def create_property(tx, id_renter, id_acc):
    tx.run(
        "match (r:renter), (a:accommodation) where r.id = $id_renter AND a.id=$id_acc CREATE (r) -[:OWNS]->(a)"
        , id_renter=id_renter, id_acc=id_acc)

def main():
    """
    acc = pd.read_csv("dataset/base/listings.csv")
    acc = cleaning_accommodations(acc)
    acc.to_csv("dataset/accommodations.csv")
    acc = avg_count_ratings()
    acc.to_csv("dataset/accommodations.csv")

    """
    accommodations_to_mongo()


if __name__ == '__main__':
    main()
