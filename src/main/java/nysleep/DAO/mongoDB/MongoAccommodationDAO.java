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
import java.util.LinkedList;
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
        doc.append("reservations", reservations);
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
    public void deleteAccommodation(int accID) {
        Document deleteQuery = new Document("_id",new Document("$eq",accID));
        deleteDoc(deleteQuery, COLLECTION);
    }

    @Override
    public void updateRating(Accommodation acc, double rating) {
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));  //search query

        acc.setRating(rating);
        Document newDoc = toDoc(acc);  //updated doc
        Document updateQuery = new Document("$set",newDoc); //update query

        updateDoc(searchQuery,updateQuery, COLLECTION);
    }

    @Override
    public void updateAccommodation(Accommodation oldAcc, Accommodation newAcc) {
        //Need to transform accommodation in search query and update query
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
    public Document getAccommodation(int accID) {
        Document searchQuery = new Document("_id",new Document("$eq",accID));
        Document doc = readDoc(searchQuery,COLLECTION);
        return doc;
    }

    public List<Document> getSearchedAcc(LocalDate startDate,LocalDate endDate,int numPeople,String neighborhood,double price,int skip,int limit) {
        return null;
    }

    //Get all the accommodations belonging to a renter
    public List<Document> getSearchedAcc(int renterID,int skip, int limit) {
        Document searchQuery = new Document("renter.id",new Document("$eq",renterID));
        ArrayList<Document> docs = readDocs(searchQuery,COLLECTION,skip,limit);
        return docs;
    }


}
