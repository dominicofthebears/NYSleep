import csv
from datetime import datetime, timedelta
import json
import random
import re

import pandas as pd
import pymongo
from neo4j import GraphDatabase


def random_date(start, end):
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = random.randrange(int_delta)
    return start + timedelta(seconds=random_second)


def main():
    reviews_dframe = pd.read_csv("dataset/base/reviews.csv")
    reviews_dframe["Review"] = reviews_dframe["Review"].apply(lambda x: re.sub("<br/>|<br>", "", str(x)))
    reviews_dframe["Review"] = reviews_dframe["Review"].apply(lambda x: re.sub(",  ", "", str(x)))
    accommodations_dframe = pd.read_csv("dataset/cleaned/accommodations.csv")
    users_dframe = pd.read_csv("dataset/cleaned/users.csv")
    acc_ids = list(accommodations_dframe["id"])
    usr_ids = [i for i in range(0, 9999)]

    final_reviews = pd.DataFrame(columns=["id_acc", "customer", "comment", "rate", "date"])

    for i in range(0, len(reviews_dframe)):
        user_id = random.choice(usr_ids)
        user = users_dframe.loc[users_dframe['id'] == user_id]
        user_dict = {"id": user_id, "first_name": user.first_name,
                     "last_name": user.last_name, "country": user.country}

        user_string = ""
        for key, value in user_dict.items():
            if key == "country":
                user_string += str(key) + " : " + str(value)
            else:
                user_string += str(key) + " : " + str(value) + " , "

        date = random_date(datetime(2015, 1, 1), datetime(2023, 1, 1))
        row = [random.choice(acc_ids), '{' + user_string + '}', reviews_dframe["Review"].loc[i],
               reviews_dframe["Rating"].loc[i], str(date)]
        final_reviews.loc[i] = row

    final_reviews.to_csv("./dataset/cleaned_reviews.csv", index=False, quoting=csv.QUOTE_MINIMAL)


def reviews_to_mongo():
    reviews = pd.read_csv("dataset/cleaned/reviews.csv")
    accommodations = pd.read_csv("dataset/cleaned/accommodations.csv") #we added this later because we had no accommodation name
    myclient = pymongo.MongoClient("mongodb://172.16.5.38:27017,172.16.5.39:27017,172.16.5.40:27017/")
    mydb = myclient["NYSleep"]
    mycol = mydb["reviews"]

    for i in range(0, len(reviews)):
        review = reviews.loc[i]
        cust = re.sub("}", "", review["customer"]).split()
        date = datetime.strptime(review["date"], '%Y-%m-%d')
        if len(cust) > 15:
            country = cust[14] + " " + cust[-1] #managing 2-length words country names
        else:
            country = cust[14]
        country = (re.sub(":", "", country)).strip()
        accommodation = accommodations[accommodations["id"] == review["id_acc"]]
        customer_dict = {"id": int(cust[2]), "first_name": cust[6], "last_name": cust[10], "country": str(country)}
        acc_dict = {"id": int(review["id_acc"]), "name" : str(accommodation["name"].values[0])}
        object_dict = {"_id" : int(i), "accommodation": acc_dict, "customer": customer_dict, "comment": review["comment"],
                       "rate": int(review["rate"]), "date": date}
        mycol.insert_one(object_dict)
        print(i)

def reviews_to_neo4j_csv():
    reviews = pd.read_csv("dataset/cleaned/reviews.csv")
    reviews_neo4j = pd.DataFrame(columns=["id_cust", "id_acc", "rate", "date"])
    for i in range(0, len(reviews)):
        review = reviews.loc[i]
        cust = re.sub("}", "", review["customer"]).split()[2]
        row = [cust, review["id_acc"], review["rate"], datetime.strptime(review["date"], '%Y-%m-%d')]
        reviews_neo4j.loc[i] = row

    reviews_neo4j.to_csv("dataset/neo4j/reviews_neo4j.csv", index=False)

def insert_rev_into_neo4j():
    URI = "bolt://172.16.5.40:7687"
    AUTH = ("neo4j", "NY_Sleep")
    reviews = pd.read_csv("dataset/neo4j/reviews_neo4j.csv")
    for i in range(0, len(reviews)):
        rev = reviews.loc[i]
        with GraphDatabase.driver(URI, auth=AUTH) as driver:
            with driver.session() as session:
                session.execute_write(create_rev, rev["date"], rev["rate"], rev["id_cust"], rev["id_acc"])
                print("Inserito" + str(i))

def create_rev(tx, date, rate, id_cust, id_acc):
    tx.run("match (c:customer), (a:accommodation) where c.id = $id_cust AND a.id=$id_acc CREATE (c) -[:REVIEWS {rate: $rate, date: Date($date)}]->(a)"
        ,id_cust=id_cust, id_acc=id_acc, rate=rate, date=date)



if __name__ == '__main__':
    reviews_to_mongo()