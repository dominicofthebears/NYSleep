import csv
from datetime import timedelta, datetime

import pandas as pd
import pymongo


def create_reservations():
    accommodations = pd.read_csv("dataset/cleaned/accommodations.csv")
    reviews = pd.read_csv("dataset/cleaned/cleaned_reviews.csv")
    users = pd.read_csv("dataset/cleaned/users.csv")
    reservations = pd.DataFrame(columns=["id", "customer", "accommodation", "start_date", "end_date", "cost"])

    for i in range(0, len(reviews)):
        res = []
        review = reviews.loc[i]

        user_id = review.customer.split()[2]
        acc_id = review["id_acc"]
        date = review["date"]

        user = users[users["id"] == user_id]
        acc = accommodations[accommodations["id"] == acc_id]

        start_date = datetime.strptime(date, '%Y-%m-%d') - timedelta(days=7)
        end_date = datetime.strptime(date, '%Y-%m-%d') - timedelta(days=4)

        cost = float(acc["price"].item() * 3)  # multiplying by the number of days of the stay

        res.append(i)
        res.append(user_id)
        res.append(acc_id)
        res.append(start_date)
        res.append(end_date)
        res.append(cost)
        reservations.loc[i] = res

    reservations.to_csv("dataset/cleaned/reservations.csv", index=False)


def reservations_to_mongo():
    reservations = pd.read_csv("dataset/cleaned/reservations.csv")
    users = pd.read_csv("dataset/cleaned/users.csv")
    accommodations = pd.read_csv("dataset/cleaned/accommodations.csv")

    myclient = pymongo.MongoClient("mongodb://172.16.5.38:27017,172.16.5.39:27017,172.16.5.40:27017/")
    mydb = myclient["NYSleep"]
    mycol = mydb["reservations"]

    for i in range(0, len(reservations)):
        res = reservations.loc[i]
        user = users[users["id"] == res["customer"]]
        acc = accommodations[accommodations["id"] == res["accommodation"]]
        user_dict = {"id": int(user["id"].iloc[0]), "first_name": user["first_name"].iloc[0],
                     "last_name": user["last_name"].iloc[0], "country": user["country"].iloc[0]}
        acc_dict = {"id": int(acc["id"].iloc[0]), "name": acc["name"].iloc[0],
                    "neighborhood": acc["neighborhood"].iloc[0]}
        object_dict = {"_id": int(res["id"]), "customer": user_dict, "accommodation": acc_dict,
                       "start_date": datetime.strptime(res["start_date"], '%Y-%m-%d')
            , "end_date": datetime.strptime(res["end_date"], '%Y-%m-%d'), "cost": float(res["cost"])}
        mycol.insert_one(object_dict)
        print(i)


if __name__ == '__main__':
    reservations_to_mongo()
