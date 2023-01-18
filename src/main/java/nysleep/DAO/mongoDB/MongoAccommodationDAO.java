package nysleep.DAO.mongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import nysleep.DAO.AccommodationDAO;
import nysleep.DTO.AccommodationDTO;
import nysleep.DTO.AccommodationDetailsDTO;
import nysleep.DTO.PageDTO;

import nysleep.model.Accommodation;

import nysleep.DAO.base.MongoBaseDAO;

import nysleep.model.Renter;
import nysleep.model.Reservation;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        List<Document> reservations = new ArrayList<Document>();
        for (Reservation res : acc.getReservations()) {
            reservations.add(new Document("start_Date", res.getStartDate())
                    .append("end_Date", res.getEndDate()));
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
        Document deleteQuery = new Document("_id",new Document("$eq",acc));
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
        Document updateQuery = new Document("$set",newDoc); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }

//Get accommodation based on
    public List<Document> getAccHomePage(int skip, int limit) {
        Document searchQuery = new Document();
        List<Document> docs = readDocs(searchQuery,COLLECTION,skip,limit);
        return docs;
    }


    //Get accommodation details
    public Document getAccommodation(Accommodation acc) {
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));
        Document doc = readDoc(searchQuery,COLLECTION);
        return doc;
    }

    public List<Document> getSearchedAcc(LocalDate startDate,LocalDate endDate,int numPeople,String neighborhood,double price,int skip,int limit) {
        return null;
    }

    //Get all the accommodations belonging to a renter
    public List<Document> getSearchedAcc(Renter renter, int skip, int limit) {
        Document searchQuery = new Document("renter.id",new Document("$eq",renter.getId()));
        ArrayList<Document> docs = readDocs(searchQuery,COLLECTION,skip,limit);
        return docs;
    }
    public List<Document> getSearchedAcc(Renter renter) {
        Document searchQuery = new Document("renter.id",new Document("$eq",renter.getId()));
        ArrayList<Document> docs = readDocs(searchQuery,COLLECTION);
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

}
