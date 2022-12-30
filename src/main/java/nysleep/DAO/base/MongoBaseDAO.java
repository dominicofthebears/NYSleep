package nysleep.DAO.base;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public abstract class MongoBaseDAO {
        private String connection = "mongodb://localhost:27017";
        public MongoBaseDAO(){

        }
        public MongoBaseDAO(String connection){
            this.connection = connection;
        }
        public String getConnection() {
            return connection;
        }
        public MongoClient connect() {
            MongoClient client = MongoClients.create(connection);
            return client;
        }
        public MongoDatabase connect(String dbName) {
            MongoClient myClient = MongoClients.create(connection);
            MongoDatabase db = myClient.getDatabase(dbName);
            return db;
        }
        public MongoCollection<Document> connect(String dbName, String collectionName) {
            MongoClient myClient = MongoClients.create(connection);
            MongoDatabase db = myClient.getDatabase(dbName);
            MongoCollection<Document> collection  = db.getCollection(collectionName);
            return collection;
        }
}

