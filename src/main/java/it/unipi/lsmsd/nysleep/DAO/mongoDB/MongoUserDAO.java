package it.unipi.lsmsd.nysleep.DAO.mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.unipi.lsmsd.nysleep.DAO.UserDAO;
import it.unipi.lsmsd.nysleep.DAO.base.MongoBaseDAO;
import it.unipi.lsmsd.nysleep.model.Admin;
import it.unipi.lsmsd.nysleep.model.Renter;

import it.unipi.lsmsd.nysleep.model.RegisteredUser;
import it.unipi.lsmsd.nysleep.model.Customer;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class MongoUserDAO extends MongoBaseDAO implements UserDAO {
    private  final String COLLECTION= "users";

    private static Document toDoc(RegisteredUser registeredUser) {
        //Convert the model object in a document
        Document doc = new Document("_id",registeredUser.getId())
                .append("first_name",registeredUser.getFirstName())
                .append("last_name",registeredUser.getLastName())
                .append("email",registeredUser.getEmail())
                .append("password",registeredUser.getPassword())
                .append("url_prof_pic",registeredUser.getUrl_prof_pic())
                .append("type",registeredUser.getType());
        if( registeredUser instanceof Customer){
            doc.append("address",((Customer) registeredUser).getAddress())
                    .append("country",((Customer) registeredUser).getCountry())
                    .append("phone",((Customer) registeredUser).getPhone());
        }
        else if(registeredUser instanceof Renter){
            doc.append("work_email",((Renter) registeredUser).getWorkEmail())
                    .append("phone",((Renter) registeredUser).getPhone());
        }
        else if(registeredUser instanceof Admin){
            doc.append("title",((Admin) registeredUser).getTitle());
        }
        return doc;
    }


    @Override
    public void register(RegisteredUser registeredUser) {
        Document doc = toDoc(registeredUser);
        insertDoc(doc, COLLECTION);
    }

    public RegisteredUser authenticate(String email,String password) {
        Document searchQuery = new Document("email",new Document("$eq",email))
                .append("password",new Document("$eq",password));

        List<Document> docs = readDocs(searchQuery,COLLECTION);
        Document doc = docs.get(0);

        RegisteredUser registeredUser;

        System.out.println(doc.get("type"));

    if(doc.get("type").equals("customer")) {
             registeredUser = new Customer(
                    (int) doc.get("_id"),
                    (String) doc.get("first_name"),
                    (String) doc.get("last_name"),
                    (String) doc.get("email"),
                    (String) doc.get("url_profile_pic"),
                    (String) doc.get("password"),
                    (String) doc.get("type"),
                    (String) doc.get("address"),
                    (String) doc.get("country"),
                    (String) doc.get("phone")
             );
        }else if (doc.get("type").equals("renter")){
            registeredUser = new Renter(
                    (int) doc.get("_id"),
                    (String) doc.get("first_name"),
                    (String) doc.get("last_name"),
                    (String) doc.get("email"),
                    (String) doc.get("url_profile_pic"),
                    (String) doc.get("password"),
                    (String) doc.get("type"),
                    (String) doc.get("work_email"),
                    (String) doc.get("phone")
            );
        }else if(doc.get("type").equals("customer")){
            registeredUser = new Admin(
                    (int) doc.get("_id"),
                    (String) doc.get("first_name"),
                    (String) doc.get("last_name"),
                    (String) doc.get("email"),
                    (String) doc.get("url_profile_pic"),
                    (String) doc.get("password"),
                    (String) doc.get("type"),
                    (String) doc.get("title")
            );
        }else
        {
            registeredUser = null;
        }

        return registeredUser;
    }
    public Document getUser(RegisteredUser user) {
        Document searchQuery = new Document("_id",new Document("$eq", user.getId()));
        Document doc = readDoc(searchQuery,COLLECTION);
        return doc;
    }

    @Override
    public void modifyAccountInfo(RegisteredUser oldUser, RegisteredUser newUser) {
        Document searchQuery = new Document("_id",new Document("$eq",oldUser.getId()));  //search query
        Document newDoc = toDoc(newUser);  //updated doc
        newDoc.remove("_id");
        newDoc.remove("url_profile_pic");
        newDoc.remove("type");
        Document updateQuery = new Document("$set",newDoc); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }

    @Override
    public void deleteAccount(RegisteredUser user) {
        Document deleteQuery = new Document("_id",new Document("$eq",user.getId()));
        deleteDoc(deleteQuery, COLLECTION);
    }



    //controllo per verificare se una mail sia stata già utilizzata
    public boolean checkEmail(String email){
        MongoClient myClient = MongoClients.create(connection);
        MongoDatabase db = myClient.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);
        Document search_email = new Document("email", new Document("$eq",email));
        if (readDoc(search_email, COLLECTION) == null){
            return false;
        }
        else{
            return true;
        }
    }

    public String getCollection(){
        return COLLECTION;
    }

}


