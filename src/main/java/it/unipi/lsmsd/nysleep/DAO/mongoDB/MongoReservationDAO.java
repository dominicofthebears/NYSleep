package it.unipi.lsmsd.nysleep.DAO.mongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import it.unipi.lsmsd.nysleep.DAO.ReservationDAO;
import it.unipi.lsmsd.nysleep.DAO.base.MongoBaseDAO;
import it.unipi.lsmsd.nysleep.model.*;

import org.bson.Document;

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

        Document doc = new Document("_id", res.getId())
                .append("customer",customerDoc)
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
        Document doc = new Document("_id",new Document("$eq",res.getId()));
        deleteDoc(doc,COLLECTION);
    }

    public void modifyReservation(Reservation oldReservation, Reservation newReservation) {
        Document searchQuery = new Document("_id",new Document("$eq",oldReservation.getId()));  //search query
        Document newDoc = toDoc(newReservation);  //updated doc
        Document updateQuery = new Document("$set", newDoc); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }

    public void deleteAccReservation(Accommodation acc){
        Document doc = new Document("accommodation.id",new Document("$eq",acc.getId()));
        deleteDoc(doc,COLLECTION);
    }

    public List<Document> getCustomerReservations(Customer customer) {

        Document searchQuery = new Document("customer.id",new Document("$eq",customer.getId()));
        List<Document> docs = readDocs(searchQuery,COLLECTION);

        return docs;
    }

    public List<Document> getAccReservations(Accommodation acc){
        Document searchQuery = new Document("accommodation.id",new Document("$eq",acc.getId()));
        List<Document> docs = readDocs(searchQuery,COLLECTION);
        return docs;
    }


    public Document custWhoHasSpentTheMost(){
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

        Document doc =null;
        while(iterator.hasNext()){
            doc =(Document) iterator.next();
        }
        return doc;
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
            docs.add(doc);
        }
        return docs;

    }

    public Document neighborhoodRentedByMostNumberOfCountries(){
        MongoReservationDAO mongoReservationDAO = new MongoReservationDAO();
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);
        AggregateIterable docsIterable = collection.aggregate(Arrays.asList(new Document("$group",
                        new Document("_id", new Document("neighborhood", "$accommodation.neighborhood") .append("country", "$customer.country"))),
                new Document("$group", new Document("_id", new Document("neigborhood", "$_id.neighborhood")) .append("num_countries", new Document("$sum", 1L))),
                new Document("$sort", new Document("num_countries", -1L)),
                new Document("$limit", 1L)));

        MongoCursor iterator = docsIterable.iterator();
        Document doc = (Document) iterator.next();

        return doc;
    }

    public String getCollection(){
        return COLLECTION;
    }

    public Document neighborhoodRentedByTheMostNumberOfCountries() {
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);

        String jsonGroupId = "{\"neighborhood\":\"$accommodation.neighborhood\",\"country\":\"$customer.country\"}";
        Document groupId = Document.parse(jsonGroupId);

        String jsonGroupId2 = "{\"neighborhood\":\"$_id.neighborhood\"}";
        Document groupId2 = Document.parse(jsonGroupId2);
        AggregateIterable docsIterable = collection.aggregate(
                Arrays.asList(
                        Aggregates.group(groupId),
                        Aggregates.group(groupId2,Accumulators.sum("num_countries",1)),
                        Aggregates.sort(Sorts.descending("num_countries")),
                        Aggregates.limit(1)
                )
        );
        Document doc;
        Iterator iterator = docsIterable.iterator();
        if(iterator.hasNext()) {
            doc = (Document) iterator.next();
            return doc;
        }else
        {return null;}
    }

    public Document mostReservedAccommodationForSeason(){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);

        String jsonProject = "{\"acc\":\"$accommodation.id\",\"month\":{$month:\"$start_date\"},\"neighborhood\":\"$accommodation.neighborhood\",\"accName\":\"$accommodation.name\"}";
        Document projectDoc = Document.parse(jsonProject);

        String jsonMatch = "{\"$or\":[{\"month\":12},{\"month\":1},{\"month\":2}]}";
        Document matchDoc = Document.parse(jsonMatch);

        String jsonAddToSetDoc = "{\"name\":\"$accName\",\"neighborhood\":\"$neighborhood\"}";
        Document addToSetDoc = Document.parse(jsonAddToSetDoc);
        AggregateIterable docsIterable = collection.aggregate(
                Arrays.asList(
                    Aggregates.project(projectDoc),
                        Aggregates.match(matchDoc),
                        Aggregates.group("$acc",Accumulators.sum("num_res",1),Accumulators.addToSet("accommodation",addToSetDoc)),
                        Aggregates.sort(Sorts.descending("num_res")),
                        Aggregates.limit(1)
                )
        );
        Document doc;
        Iterator iterator = docsIterable.iterator();
        if(iterator.hasNext()) {
            doc = (Document) iterator.next();
            return doc;
        }else
        {return null;}
    }

}


