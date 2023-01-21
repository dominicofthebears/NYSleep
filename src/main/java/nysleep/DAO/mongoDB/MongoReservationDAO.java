package nysleep.DAO.mongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Sorts;
import nysleep.DAO.ReservationDAO;
import nysleep.DAO.base.MongoBaseDAO;

import nysleep.DTO.PageDTO;
import nysleep.DTO.ReservationDTO;

import nysleep.model.Accommodation;
import nysleep.model.Customer;
import nysleep.model.Reservation;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class MongoReservationDAO extends MongoBaseDAO implements ReservationDAO {
    private final String COLLECTION = "reservations";

    private static Document toDoc(Reservation res){
        //Convert the model object in a document

        Document customerDoc = new Document("_id",res.getCustomer().getId())
                .append("first_name",res.getCustomer().getFirstName())
                .append("last_name",res.getCustomer().getLastName())
                .append("country",res.getCustomer().getCountry());

        Document accDoc = new Document("_id",res.getAccommodation().getId())
                .append("name",res.getAccommodation().getName())
                .append("neighborhood",res.getAccommodation().getNeighborhood());

        Document doc = new Document("customer",customerDoc)
                .append("accommodation",accDoc)
                .append("start_date",res.getStartDate())
                .append("end_date",res.getEndDate())
                .append("cost",res.getTotalCost());
        return doc;
    }


    @Override
    public void createReservation(Reservation res) {
        Document doc = toDoc(res);
        insertDoc(doc, COLLECTION);
    }

    @Override
    public void deleteReservation(Reservation res) {
        Document doc = toDoc(res);
        insertDoc(doc, COLLECTION);
    }

    public void deleteAccReservation(Accommodation acc){
        Document doc = new Document("accommodation.id",new Document("$eq",acc.getId()));
        deleteDoc(doc,COLLECTION);
    }

    public List<Document> getCustomerReservations(Customer customer) {

        Document searchQuery = new Document("customer.id",new Document("$eq",customer.getId()));
        ArrayList<Document> docs = readDocs(searchQuery,COLLECTION);

        return docs;
    }
    public List<Document> getRenterReservations(Customer customer) {

        Document searchQuery = new Document("customer.id",new Document("$eq",customer.getId()));
        ArrayList<Document> docs = readDocs(searchQuery,COLLECTION);

        return docs;
    }

    public List<Document> getAccReservations(Accommodation acc){

        Document searchQuery = new Document("accommodation.id",new Document("$eq",acc.getId()));
        ArrayList<Document> docs = readDocs(searchQuery,COLLECTION);
        return docs;
    }

    public List<Document> customerWhoHasSpentTheMost(){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);

        //String groupJson ="{\"$group\":{\"_id\":{\"cust_id\":\"$customer.id\",\"first_name\":\"$customer.first_name\",\"last_name\":\"$customer.last_name\",\"total_spent\":{\"$sum\":\"$cost\"}}}}";
        //Document group = Document.parse(groupJson);
        String groupIDJson ="{\"cust_id\":\"$customer.id\",\"first_name\":\"$customer.first_name\",\"last_name\":\"$customer.last_name\"}";//,\"total_spent\":{\"$sum\":\"$cost\"}}";
        Document groupID = Document.parse(groupIDJson);
        Bson group = Aggregates.group(groupID);

        //String sortJson = "{\"$sort\":{\"_id.total_spent\":-1}}";
        //Document sort  = Document.parse(sortJson)";
        //Bson sort =  Aggregates.sort(Sorts.ascending());
        Bson sort = Aggregates.sort(Sorts.descending("_id.total_spent"));

        //String limitJson = "{\"$limit\":1}";
        //Document limit = Document.parse(limitJson);
        Bson limit = Aggregates.limit(1);
        List<Bson> stages = Arrays.asList(group, sort, limit);
        AggregateIterable<Document> docsIterable =  collection.aggregate(
                Arrays.asList(
                        Aggregates.group(groupID, Accumulators.sum("total_spent", "$cost")),
                        Aggregates.sort(Sorts.descending("total_spent")),
                        Aggregates.limit(1)
                )
        );

        ArrayList<Document> docs = new ArrayList<>();
        while(docsIterable.iterator().hasNext()){
            Document doc =  docsIterable.iterator().next();
            System.out.println(doc);
            docs.add(doc);
        }
        return docs;
    }
}
