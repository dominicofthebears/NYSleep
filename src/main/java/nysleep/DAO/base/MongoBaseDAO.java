package nysleep.DAO.base;

import com.mongodb.ReadPreference;
import com.mongodb.client.*;
import org.bson.Document;


import java.util.ArrayList;
import java.util.Iterator;


public abstract class MongoBaseDAO{
    protected static String connection = "mongodb://172.16.5.38:27017,172.16.5.39:27017,172.16.5.40:27017/" +
            "?retryWrites=true&w=majority&readPreference=nearest";
    protected static String dbName = "NYSleep";
    /*protected static String connection="mongodb://localhost:27017";
    protected static String dbName = "NYTest";*/
    protected static MongoClient client;
    protected static ClientSession session;
    protected static String COLLECTION;
    public MongoBaseDAO(){
        MongoClient client = MongoClients.create(this.connection);
        this.client = client;
        this.session = client.startSession();
    }
    public MongoBaseDAO(String connection){
        MongoClient client = MongoClients.create(connection);
        this.client = client;
        this.connection = connection;
        this.session = client.startSession();
    }

    public String getConnectionName(){return connection;}

    public MongoClient getConnection() {
            return client;
        }

    public void closeConnection(){client.close();}

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {return dbName;}

    public ClientSession getSession(){return this.session;}

    public String getCollection(){
        return COLLECTION;
    }


    public void startTransaction(){session.startTransaction();}

    public void commitTransaction(){session.commitTransaction();}

    public void abortTransaction(){session.abortTransaction();}

    public static void insertDoc(Document doc,String collectionName) {
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(session,doc);
    }

    public static void deleteDoc(Document doc,String collectionName){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection  = db.getCollection(collectionName);
        collection.deleteOne(session,doc);
    }

    public static void updateDoc(Document oldDoc,Document query, String collectionName) {
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.updateOne(session,oldDoc, query);
    }

    public static ArrayList<Document> readDocs(Document query, String collectionName) {
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection;

        if(collectionName.equals("accommodations")){
            collection = db.getCollection(collectionName).withReadPreference(ReadPreference.primary()); //reading accommodations from primary
        }
        else{
            collection = db.getCollection(collectionName);
        }

        Iterator<Document> docsIterator = collection.find(query).iterator();  //Extract all the document found
        ArrayList<Document> docs = new ArrayList<>();
        while (docsIterator.hasNext()) {                          //iterate all over the iterator of document
            docs.add((Document) docsIterator.next());
        }
        return docs;
    }

    public static ArrayList<Document> readDocs(Document query, String collectionName,int skip,int limit) {
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection;

        if(collectionName.equals("accommodations")){
            collection = db.getCollection(collectionName).withReadPreference(ReadPreference.primary()); //reading accommodations from primary
        }
        else{
            collection = db.getCollection(collectionName);
        }

        Iterator<Document> docsIterator = collection.find(query).skip(skip).limit(limit).iterator();  //Extract all the document found
        ArrayList<Document> docs = new ArrayList<>();
        while(docsIterator.hasNext()){                          //iterate all over the iterator of document
                    docs.add((Document) docsIterator.next());
                }
        
        return docs;
    }

    public static Document readDoc(Document query, String collectionName) {
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection;

        if(collectionName.equals("accommodations")){
            collection = db.getCollection(collectionName).withReadPreference(ReadPreference.primary()); //reading accommodations from primary
        }
        else{
            collection = db.getCollection(collectionName);
        }

        Document doc = collection.find(query).first();  //Extract all the document found
        return doc;
    }

    public int getLastId(String COLLECTION){
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);
        Document sort = new Document("_id",-1);
        Document last = (Document) collection.find().sort(sort).limit(1);
        return last.getInteger("_id");
    }




}

