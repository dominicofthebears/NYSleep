import random

import pandas as pd
import pymongo
from neo4j import GraphDatabase
from randomuser import RandomUser


def create_customers():
    user_list = RandomUser.generate_users(5000)
    users = pd.read_csv('dataset/cleaned/users.csv')
    dataframe = pd.DataFrame(columns=["id", "first_name", "last_name", "email", "password",
                                      "url_profile_pic", "type", "address", "country", "phone", "work_email",
                                      "title"])

    for i in range(0, 5000):
        user = user_list[i]
        row = [str(i + 5000), user.get_first_name(), user.get_last_name(), user.get_email(), user.get_password(),
               user.get_picture(size="thumbnail"), "customer", user.get_street(), user.get_country(), user.get_phone(),
               " ", " "]
        dataframe.loc[i] = row

    users = pd.concat([users, dataframe], axis=0)
    users.to_csv("./dataset/users.csv", index=False)


def create_renters():
    user_list = RandomUser.generate_users(2500)
    users = pd.read_csv('dataset/cleaned/users.csv')
    dataframe = pd.DataFrame(columns=["id", "first_name", "last_name", "email", "password",
                                      "url_profile_pic", "type", "address", "country", "phone", "work_email",
                                      "title"])

    for i in range(0, 2500):
        user = user_list[i]
        row = [str(i + 10000), user.get_first_name(), user.get_last_name(), user.get_email(), user.get_password(),
               user.get_picture(size="thumbnail"), "renter", " ", " ", user.get_phone(),
               user.get_first_name() + "." + user.get_last_name() + "@NYSleep.com", " "]
        dataframe.loc[i] = row

    users = pd.concat([users, dataframe], axis=0)
    users.to_csv("./dataset/users.csv", index=False)


def create_admins():
    user_list = RandomUser.generate_users(100)
    users = pd.read_csv('dataset/cleaned/users.csv')
    dataframe = pd.DataFrame(columns=["id", "first_name", "last_name", "email", "password",
                                      "url_profile_pic", "type", "address", "country", "phone", "work_email",
                                      "title"])

    for i in range(0, 100):
        user = user_list[i]
        test_list = ["manager", "supervisor", "analyst"]
        random_role = random.choice(test_list)
        row = [str(i + 12500), user.get_first_name(), user.get_last_name(), user.get_email(), user.get_password(),
               user.get_picture(size="thumbnail"), "admin", " ", " ", " ",
               " ", random_role]
        dataframe.loc[i] = row

    users = pd.concat([users, dataframe], axis=0)
    users.to_csv("./dataset/users.csv", index=False)


def users_to_mongo():
    users = pd.read_csv("dataset/cleaned/users.csv")
    myclient = pymongo.MongoClient("mongodb://172.16.5.38:27017,172.16.5.39:27017,172.16.5.40:27017/")
    mydb = myclient["NYSleep"]
    mycol = mydb["users"]

    for i in range(0, len(users)):
        user = users.loc[i]
        if user["type"] == "customer":
            object_dict = {"_id": int(user["id"]), "first_name": user["first_name"], "last_name": user["last_name"],
                           "email": user["email"], "password": user["password"], "url_profile_pic": user["url_profile_pic"],
                           "type": user["type"], "address": user["address"], "country": user["country"],
                           "phone": user["phone"]}
        else:
            if user["type"] == "renter":
                object_dict = {"_id": int(user["id"]), "first_name": user["first_name"], "last_name": user["last_name"],
                               "email": user["email"], "password": user["password"],
                               "url_profile_pic": user["url_profile_pic"],
                               "type": user["type"], "phone": user["phone"], "work_email": user["work_email"]}

            else:
                object_dict = {"_id": int(user["id"]), "first_name": user["first_name"], "last_name": user["last_name"],
                               "email": user["email"], "password": user["password"],
                               "url_profile_pic": user["url_profile_pic"],
                               "type": user["type"], "title": user["title"]}

        mycol.insert_one(object_dict)
        print(i)

def neo4j_customers_csv():
    users = pd.read_csv("dataset/cleaned/users.csv")
    customers = users[users["type"] == "customer"]
    customers = customers[["id", "first_name", "last_name"]]
    customers.to_csv("dataset/neo4j/customers_neo4j.csv", index=False)


def neo4j_renters_csv():
    users = pd.read_csv("dataset/cleaned/users.csv")
    customers = users[users["type"] == "renter"]
    customers = customers[["id", "first_name", "last_name"]]
    customers.to_csv("dataset/neo4j/renters_neo4j.csv", index=False)

    accommodations = pd.read_csv("dataset/cleaned/accommodations.csv")
    property_dataframe = pd.DataFrame(columns=["id_acc", "id_renter"])
    renters_ids = list(customers["id"])
    for i in range (0, len(accommodations)):
        acc = accommodations.loc[i]
        renter = random.choice(renters_ids)
        row = [acc["id"], renter]
        property_dataframe.loc[i] = row

    property_dataframe.to_csv("dataset/neo4j/property_neo4j.csv", index=False)


def create_neo4j_customer_renter(tx, id, first_name, last_name):
    tx.run(
        "CREATE (c:customer {id: $id, first_name: $first_name, last_name: $last_name})",
        id=id, first_name=first_name, last_name=last_name)


def insert_customers_renters_neo4j(type):
    URI = "bolt://172.16.5.40:7687"
    AUTH = ("neo4j", "NY_Sleep")
    if(type == "renters"):
        users = pd.read_csv("dataset/neo4j/renters_neo4j.csv")
    else:
        users = pd.read_csv("dataset/neo4j/customers_neo4j.csv")
    for i in range(0, len(users)):
        usr = users.loc[i]
        with GraphDatabase.driver(URI, auth=AUTH) as driver:
            with driver.session() as session:
                session.execute_write(create_neo4j_customer, usr["id"], usr["first_name"], usr["last_name"])
                print("Inserito" + str(usr["id"]))

def create_neo4j_customer(tx, id, first_name, last_name):
    tx.run(
        "CREATE (c:renter {id: $id, first_name: $first_name, last_name: $last_name})",
        id=id, first_name=first_name, last_name=last_name)



def main():

    users = pd.read_csv("./dataset/cleaned/users.csv")
    users_id = list(users["id"])
    accommodations = pd.read_csv("./dataset/cleaned/accommodations.csv")
    reviews = pd.read_csv("./dataset/cleaned/cleaned_reviews.csv")
    reservations = pd.read_csv("./dataset/cleaned/reservations.csv")
    listings = pd.read_csv("dataset/base/listings.csv")
    names = [name for name in listings["name"]]
    names = names[:len(accommodations)]
    ids = [i for i in range (0, len(accommodations))]
    #accommodations.insert(0, "id", ids, allow_duplicates=False)
    accommodations["name"] = names

    for i in range (0, len(reviews)):
        if int(reviews["customer"].loc[i].split()[2]) not in users_id:
            reviews.drop(index=i, axis=0, inplace=True)
    
    for i in range (0, len(reservations)):
        if reservations["customer"].loc[i] not in users_id:
            reservations.drop(index=i, axis=0, inplace=True)
    reservations.to_csv("./dataset/cleaned/reservations.csv", index=False)
    reviews.to_csv("./dataset/cleaned/reviews.csv", index=False)
    accommodations.to_csv("./dataset/cleaned/accommodations.csv", index=False)
    users.to_csv("./dataset/cleaned/users.csv", index=False)



if __name__ == '__main__':
    users_to_mongo()