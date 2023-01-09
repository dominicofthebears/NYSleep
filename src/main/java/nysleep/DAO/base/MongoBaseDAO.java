package nysleep.DAO.base;

import com.mongodb.BasicDBObject;
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
    private static String dbName = "NYsleep";

    public MongoBaseDAO(){}

    protected void setConnection(String connection) {
        this.connection = connection;
    }
    protected String getConnection() {
            return connection;
        }
    protected void setDbName(String dbName) {
        this.dbName = dbName;
    }
    protected String getDbName() {
        return dbName;
    }
    protected static MongoClient connect() {
        MongoClient client = MongoClients.create(connection);
        return client;
    }
    protected static MongoDatabase connect(String dbName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        return db;
    }
    protected static MongoCollection<Document> connect(String dbName, String collectionName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection  = db.getCollection(collectionName);
        return collection;
    }
    protected static void insertDoc(Document doc,String collectionName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(doc);
        myClient.close();
    }
    protected static void deleteDoc(Document doc,String collectionName){
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection  = db.getCollection(collectionName);
        collection.deleteOne(doc);
        myClient.close();
        }
    protected static void updateDoc(Document oldDoc,Document query, String collectionName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.updateOne(oldDoc, query);
        myClient.close();
    }
    protected static ArrayList<Document> readDoc(Document query, String collectionName) {
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);

        Iterator docsIterator = collection.find(query).iterator();  //Extract all the document found
        ArrayList<Document> docs = new ArrayList<Document>();
        while(docsIterator.hasNext()){                          //iterate all over the iterator of document
                    docs.add((Document) docsIterator.next());
                }

        myClient.close();
        return docs;
    }

}

