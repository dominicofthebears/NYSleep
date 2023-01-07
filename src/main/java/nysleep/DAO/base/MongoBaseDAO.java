package nysleep.DAO.base;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


import java.util.ArrayList;
import java.util.Iterator;


public abstract class MongoBaseDAO{
    private static String connection = "mongodb://localhost:27017";
    private static String dbName = "nysleep";

    public MongoBaseDAO(){}

    public void setConnection(String connection) {
        this.connection = connection;
    }
    public String getConnection() {
            return connection;
        }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public String getDbName() {
        return dbName;
    }
    public static MongoClient connect() {
        MongoClient client = MongoClients.create(connection);
        return client;
    }
    public static MongoDatabase connect(String dbName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        return db;
    }
    public static MongoCollection<Document> connect(String dbName, String collectionName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection  = db.getCollection(collectionName);
        return collection;
    }
    public static void insertDoc(Document doc,String collectionName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(doc);
        myClient.close();
    }
    public static void deleteDoc(Document doc,String collectionName){
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection  = db.getCollection(collectionName);
        collection.deleteOne(doc);
        myClient.close();
        }
    public static void updateDoc(Document oldDoc,Document query, String collectionName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.updateOne(oldDoc, query);
        myClient.close();
    }
    public static ArrayList<Document> readDoc(Document query, String collectionName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);

        Iterator docsIterator = collection.find().iterator();  //Extract all the document found
        ArrayList<Document> docs = new ArrayList<Document>();
        while(docsIterator.hasNext()){                          //iterate all over the iterator of document
                    docs.add((Document) docsIterator.next());
                }

        myClient.close();
        return docs;
    }
}

