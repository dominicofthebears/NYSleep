package nysleep.DAO.mongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

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


    public List<Document> custWhoHasSpentTheMost(){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);


        String groupIDJson ="{\"cust_id\":\"$customer.id\",\"first_name\":\"$customer.first_name\",\"last_name\":\"$customer.last_name\"}";
        Document groupID = Document.parse(groupIDJson);

        AggregateIterable<Document> docsIterable =  collection.aggregate(
                Arrays.asList(
                        Aggregates.group(groupID, Accumulators.sum("total_spent", "$cost")),
                        Aggregates.sort(Sorts.descending("total_spent")),
                        Aggregates.limit(1)
                )
        );

        Iterator iterator = docsIterable.iterator();


        ArrayList<Document> docs = new ArrayList<>();
        while(iterator.hasNext()){
            docs.add((Document) iterator.next());
        }
        return docs;
    }


    //most Reserved accommodation for each neighborhood
    public List<Document> mostResAccForEachNeigh (){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);

        String jsonGroupId1 = "{\"neighborhood\":\"$accommodation.neighborhood\",\"acc\":\"$accommodation.id\",\"accName\":\"$accommodation.name\"}";
        Document groupId1 = Document.parse(jsonGroupId1);


        AggregateIterable docsIterable = collection.aggregate(
                Arrays.asList(
                Aggregates.group(groupId1,Accumulators.sum("numRes",1)),
                Aggregates.sort(Sorts.descending("numRes")),
                Aggregates.group("$_id.neighborhood",Accumulators.first("most_res_acc","$_id.accName"),
                                                        Accumulators.first("num_reservation","$numRes"))
                )
        );

        Iterator iterator = docsIterable.iterator();
        List<Document> docs = new ArrayList<Document>();
        while(iterator.hasNext()){
            docs.add((Document) iterator.next());
        }
        return docs;
    }

    //Customer with highest average expense
    public Document custWithHighestAvgExpense(){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);

        String jsonGroupId = "{\"cust_id\":\"$customer.id\",\"first_name\":\"$customer.first_name\",\"last_name\":\"$customer.last_name\",\"country\":\"$customer.country\"}";
        Document groupId = Document.parse(jsonGroupId);

        String jsonFilter = "{\"num_res\":{\"$gt\":5}}";
        Document filter = Document.parse(jsonFilter);
        AggregateIterable docsIterable = collection.aggregate(
                Arrays.asList(
                        Aggregates.group(groupId,
                                Accumulators.avg("avg_cost","$cost"),
                                Accumulators.sum("num_res",1)
                        ),
                        Aggregates.match(Filters.gt("num_res",5)),
                        Aggregates.sort(Sorts.descending("avg_cost")),
                        Aggregates.limit(1)
                )
        );

        Iterator iterator = docsIterable.iterator();
        Document doc = (Document)iterator.next();
        return doc;
    }

    //Most reserving country for each neighborhood
    public List<Document>  mostResCountryForNeigh(){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);

        String jsonGroupId = "{\"neighborhood\":\"$accommodation.neighborhood\",\"country\":\"$customer.country\"}";
        Document groupId = Document.parse(jsonGroupId);
        AggregateIterable docsIterable = collection.aggregate(
                Arrays.asList(
                        Aggregates.group(groupId,Accumulators.sum("num_res",1)),
                        Aggregates.sort(Sorts.descending("num_res")),
                        Aggregates.group("$_id.neighborhood",
                                Accumulators.first("most_res_country","$_id.country"),
                                Accumulators.first("num_reservation","$num_res"))
                )
        );

        Iterator iterator = docsIterable.iterator();
        List<Document> docs = new ArrayList<Document>();

        while(iterator.hasNext()){
            Document doc = (Document) iterator.next();
            System.out.println(doc);
            docs.add(doc);
        }
        return docs;

    }

    public List<Document> accommodationRentedByMostNumberOfCountries(){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);
        String jsonGroupId = "{\"neighborhood\":\"$accommodation.neighborhood\",\"country\":\"$customer.country\"}";
        AggregateIterable docsIterable = collection.aggregate(
                Arrays.asList(
                        Aggregates.group(jsonGroupId),
                        Aggregates.group("$_id.neighborhood",
                                Accumulators.sum("num_countries", 1)),
                        Aggregates.sort(Sorts.descending("num_countries"))
                )
        );

        Iterator iterator = docsIterable.iterator();
        List<Document> docs = new ArrayList<Document>();

        while(iterator.hasNext()){
            docs.add((Document) iterator.next());
        }
        return docs;
    }

}


