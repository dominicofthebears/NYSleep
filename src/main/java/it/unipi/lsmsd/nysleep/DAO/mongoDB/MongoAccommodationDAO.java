package it.unipi.lsmsd.nysleep.DAO.mongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import it.unipi.lsmsd.nysleep.DAO.AccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.base.MongoBaseDAO;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.Accommodation;

import it.unipi.lsmsd.nysleep.model.Renter;
import it.unipi.lsmsd.nysleep.model.Reservation;
import org.bson.Document;

import java.time.LocalDate;
import java.util.*;

public class MongoAccommodationDAO extends MongoBaseDAO implements AccommodationDAO {
    private final String COLLECTION = "accommodations";

    private static Document toDoc(Accommodation acc) {
        //Convert the model Accommodation object in a document to insert in MongoDB
        Document doc = new Document("_id", acc.getId())
                .append("name", acc.getName())
                .append("neighborhood", acc.getNeighborhood())
                .append("images_URL", acc.getImagesURL())
                .append("num_beds", acc.getNumBeds())
                .append("num_rooms", acc.getNumRooms())
                .append("price", acc.getPrice())
                .append("num_reviews", acc.getNumReviews())
                .append("property_type", acc.getPropertyType())
                .append("amenities", acc.getAmenities())
                .append("rating", acc.getRating());
        List<Document> reservations = new LinkedList<Document>();
        if(acc.getReservations() != null ) {
            for (Reservation res : acc.getReservations()) {
                reservations.add(new Document("start_Date", res.getStartDate())
                        .append("end_Date", res.getEndDate()));
            }
        }
        Document renterDoc =  new Document("id",acc.getRenter().getId())
                .append("first_name",acc.getRenter().getFirstName())
                .append("last_name",acc.getRenter().getLastName())
                .append("work_email",acc.getRenter().getWorkEmail())
                .append("phone",acc.getRenter().getPhone());

        doc.append("reservations", reservations);
        doc.append("renter",renterDoc);
        return doc;
    }

    @Override
    public void createAccommodation(Accommodation acc) {
        Document doc = toDoc(acc);
        insertDoc(doc, COLLECTION);
    }

    @Override
    public void deleteAccommodation(Accommodation acc) {
        Document deleteQuery = new Document("_id",new Document("$eq",acc.getId()));
        deleteDoc(deleteQuery, COLLECTION);
    }


    @Override
    public void updateRating(Accommodation acc, double rating) {
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));  //search query
        Document updateQuery = new Document("$set",new Document("rating",rating)); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }

    public void decreaseNumReview(Accommodation acc){
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));  //search query
        Document updateQuery = new Document("$inc", new Document("num_reviews",-1)); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }
    public void incrementNumReview(Accommodation acc){
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));  //search query
        Document updateQuery = new Document("$inc", new Document("num_reviews",1)); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }

    @Override
    public void updateAccommodation(Accommodation oldAcc, Accommodation newAcc){
        Document searchQuery = new Document("_id",new Document("$eq",oldAcc.getId()));  //search query
        Document newDoc = toDoc(newAcc);  //updated doc
        newDoc.remove("_id");
        newDoc.remove("neighborhood");
        newDoc.remove("num_reviews");
        newDoc.remove("property_type");
        newDoc.remove("reservations");
        newDoc.remove("rating");
        newDoc.remove("renter");
        Document updateQuery = new Document("$set",newDoc); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }
    public void updateAccommodationRenter(Accommodation acc){
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));
        Document renterDoc = new Document("id",acc.getRenter().getId())
                .append("first_name",acc.getRenter().getFirstName())
                .append("last_name",acc.getRenter().getLastName())
                .append("work_email",acc.getRenter().getWorkEmail())
                .append("phone",acc.getRenter().getPhone());
        Document updateQuery = new Document("$set",new Document("renter",renterDoc)); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }

//Get accommodation based on
    public List<Document> getAccHomePage(int skip, int limit) {
        Document searchQuery = new Document();
        List<Document> docs = readDocs(searchQuery,COLLECTION, skip, limit);
        return docs;
    }


    //Get accommodation details
    public Document getAccommodation(Accommodation acc) {
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));
        Document doc = readDoc(searchQuery,COLLECTION);
        return doc;
    }

    public List<Document> getSearchedAcc(LocalDate startDate,LocalDate endDate,int numPeople,String neighborhood,
                                         double price, int skip, int limit) throws BusinessException {
        Document searchQuery;

        if(startDate == null || endDate == null){
            throw new BusinessException("Select start date and end date");
        }

        if(numPeople == 0){
            throw new BusinessException("Select a number of people");
        }

        if(startDate.isAfter(endDate)){
            throw new BusinessException("Start date must come before end date");
        }

        if(price < 0){
            throw new BusinessException("Maximum price must be a positive value");
        }

        try {
            if (neighborhood.equals("") && price == 0) {
                searchQuery = new Document("$and", Arrays.asList(new Document("$or", Arrays.asList(new Document("reservations.start_date",
                                        new Document("$not",
                                                new Document("$lte", endDate))),
                                new Document("reservations.end_date",
                                        new Document("$not",
                                                new Document("$gte", startDate))))),
                        new Document("num_beds", numPeople)));

            } else if (neighborhood.equals("")) {
                searchQuery = new Document("$and", Arrays.asList(new Document("$or", Arrays.asList(new Document("reservations.start_date",
                                        new Document("$not",
                                                new Document("$lte",
                                                        endDate))),
                                new Document("reservations.end_date",
                                        new Document("$not", new Document("$gte", startDate))))),
                        new Document("num_beds", numPeople),
                        new Document("price",
                                new Document("$lt", price))));
            }
            else if (price == 0) {
                searchQuery = new Document("$and", Arrays.asList(new Document("$or", Arrays.asList(new Document("reservations.start_date",
                                        new Document("$not",
                                                new Document("$lte",
                                                        endDate))),
                                new Document("reservations.end_date",
                                        new Document("$not",
                                                new Document("$gte",
                                                        startDate))))),
                        new Document("neighborhood", neighborhood),
                        new Document("num_beds", numPeople)));

            }
            else{
                searchQuery = new Document("$and", Arrays.asList(new Document("$or", Arrays.asList(new Document("reservations.start_date",
                                        new Document("$not",
                                                new Document("$lte",
                                                        endDate))),
                                new Document("reservations.end_date",
                                        new Document("$not",
                                                new Document("$gte",
                                                        startDate))))),
                        new Document("neighborhood", neighborhood),
                        new Document("num_beds", numPeople),
                        new Document("price",
                                new Document("$lt", price))));
            }
        }catch (Exception e){
            throw new BusinessException(e);
        }

        return readDocs(searchQuery,COLLECTION,skip, limit);
    }

    //Get all the accommodations belonging to a renter

    public List<Document> getSearchedAcc(Renter renter) {
        Document searchQuery = new Document("renter.id",new Document("$eq",renter.getId()));
        List<Document> docs = readDocs(searchQuery,COLLECTION);
        return docs;
    }

    public void deleteReservation(Accommodation acc,Reservation res){
        Document query = new Document("$pull",
                new Document("reservation",
                        new Document("start_date",res.getStartDate())
                        .append("end_date",res.getEndDate())
                    )
        );
        Document accDoc = new Document("_id",new Document("$eq",acc.getId()));
        updateDoc(accDoc,query,COLLECTION);
    }
    public void insertReservation(Accommodation acc, Reservation res){
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));
        Document resDoc = new Document("start_date",res.getStartDate()).append("end_date",res.getEndDate());
        Document updateQuery = new Document("$push", new Document("reservations",resDoc));
        updateDoc(searchQuery, updateQuery, COLLECTION);
    }

    public void cleanReservations(Accommodation acc) throws BusinessException {
        try{
            List<Reservation> reservations = acc.getReservations();
            if(reservations==null)
                return;
            for (Reservation res:reservations) {
                if (res.getEndDate().isBefore(LocalDate.now())){
                    reservations.remove(res);
                }
            }
            acc.setReservations(reservations);
            updateAccommodation(acc, acc);
        }catch (Exception e){
            throw new BusinessException(e);
        }
    }

    public List<Document> mostExpensiveAndLeastExpensiveAccommodationForPropertyType(){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);


        String jsonProject ="{\"id\":1,\"name\":1,\"neighborhood\":1,\"property_type\":1,\"price\":1}";
        Document projectDoc = Document.parse(jsonProject);


        AggregateIterable docsIterable = collection.aggregate(
                Arrays.asList(
                        Aggregates.project(projectDoc),
                        Aggregates.sort(Sorts.descending("price")),
                        Aggregates.group("$property_type",
                                Accumulators.last("least_expensive","$name"),
                                Accumulators.last("lowest_cost","$price"),
                                Accumulators.last("least_expensive_neigh","$neighborhood"),
                                Accumulators.first("most_expensive_name","$name"),
                                Accumulators.first("most_expensive_neigh","$neighborhood"),
                                Accumulators.first("highest_cost","$price")
                                )
                )
        );

        Iterator iterator = docsIterable.iterator();
        List<Document> docs = new ArrayList<Document>();

        while(iterator.hasNext()){
            docs.add((Document) iterator.next());
        }
        return docs;

    }

    public String getCollection(){
        return COLLECTION;
    }


}
