package it.unipi.lsmsd.nysleep.DAO.base;

import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public abstract class MongoBaseDAO{
    protected static String connection = "mongodb://172.16.5.38:27017,172.16.5.39:27017,172.16.5.40:27017/" +
            "?retryWrites=true&w=majority&readPreference=nearest";
    protected static String dbName = "NYSleep";
    protected static MongoClient client;
    public static ClientSession session;
    protected static CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),fromProviders(PojoCodecProvider.builder().automatic(true).build()));
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



    public void startTransaction(){
        session.startTransaction();
    }

    public void commitTransaction(){session.commitTransaction();}

    public void abortTransaction(){session.abortTransaction();}

    public static void insertDoc(Document doc,String collectionName) {
        MongoDatabase db = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(session,doc);
    }

    public static void deleteDoc(Document doc,String collectionName){
        MongoDatabase db = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> collection  = db.getCollection(collectionName);
        collection.deleteMany(session,doc);
    }


    public static void updateDoc(Document oldDoc,Document query, String collectionName) {
        MongoDatabase db = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.updateMany(session,oldDoc, query);
    }

    public static List<Document> readDocs(Document query, String collectionName) {
        MongoDatabase db = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> collection;

        if(collectionName.equals("accommodations")){
            collection = db.getCollection(collectionName).withReadPreference(ReadPreference.primary()); //reading accommodations from primary
        }
        else{
            collection = db.getCollection(collectionName);
        }

        Iterator<Document> docsIterator = collection.find(query).iterator();  //Extract all the document found
        List<Document> docs = new LinkedList<>();
        while (docsIterator.hasNext()) {                          //iterate all over the iterator of document
            docs.add((Document) docsIterator.next());
        }
        return docs;
    }

    public static List<Document> readDocs(Document query, String collectionName, int skip, int limit) {
        MongoDatabase db = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> collection;

        if(collectionName.equals("accommodations")){
            collection = db.getCollection(collectionName).withReadPreference(ReadPreference.primary()); //reading accommodations from primary
        }
        else{
            collection = db.getCollection(collectionName);
        }

        Iterator<Document> docsIterator = collection.find(query).skip(skip).limit(limit).iterator();  //Extract all the document found
        List<Document> docs = new LinkedList<>();
        while(docsIterator.hasNext()){                          //iterate all over the iterator of document
                    docs.add((Document) docsIterator.next());
                }
        
        return docs;
    }

    public static Document readDoc(Document query, String collectionName) {
        MongoDatabase db = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
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
        MongoDatabase db = myClient.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);
        Document sort = new Document("_id",-1);
        Iterator iterator = collection.find().sort(sort).limit(1).iterator();
        Document doc = (Document) iterator.next();
        return ((int)doc.get("_id")+1);
    }

    public static List<String>  getUniqueValues(String field, String COLLECTION){
        MongoDatabase db = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);
        DistinctIterable<String> neighborhoods = collection.distinct(field, String.class);
        List<String> result = new ArrayList<>();
        for(String s : neighborhoods){
            result.add(s);
        }
        return result;
    }




}

