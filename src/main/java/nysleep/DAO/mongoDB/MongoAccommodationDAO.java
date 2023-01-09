package nysleep.DAO.mongoDB;

import com.mongodb.client.MongoCollection;

import nysleep.DAO.AccommodationDAO;
import nysleep.DTO.AccommodationDTO;
import nysleep.DTO.PageDTO;

import nysleep.model.Accommodation;
import nysleep.model.Renter;

import nysleep.DAO.base.MongoBaseDAO;
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

    @Override
    public PageDTO<AccommodationDTO> getAccHomePage() {
        Document searchQuery = new Document();
        ArrayList<Document> docs = readDoc(searchQuery,COLLECTION);
        List<AccommodationDTO> accDTOList = new ArrayList<AccommodationDTO>();

        for(Document doc: docs){                                                    //iterate all over the documents and extract accommodations to put in the DTO
            ArrayList<String> picsURL = (ArrayList<String>) doc.get("images_URL");
            AccommodationDTO accDTO = new AccommodationDTO(
                    (int) doc.get("_id"),
                    (String) doc.get("name"),
                    (String) doc.get("neighborhood"),
                    (double) doc.get("rating"),
                    picsURL.get(0));
            accDTOList.add(accDTO);
        }

        PageDTO<AccommodationDTO> AccHomePage = new PageDTO<AccommodationDTO>();
        AccHomePage.setEntries(accDTOList);

        return AccHomePage;
    }

    @Override
    public AccommodationDTO getAccommodation(Accommodation acc) {
        Document searchQuery = new Document("_id",new Document("$eq",acc.getId()));

        ArrayList<Document> docs = readDoc(searchQuery,COLLECTION);
        Document doc = docs.get(0);

        ArrayList<String> picsURL = (ArrayList<String>) doc.get("images_URL");
        AccommodationDTO accDTO = new AccommodationDTO(
                (int) doc.get("_id"),
                (String) doc.get("name"),
                (String) doc.get("neighborhood"),
                (double) doc.get("rating"),
                picsURL.get(0) );
        return accDTO;
    }

    @Override
    public PageDTO<AccommodationDTO> getSearchedAcc(LocalDate startDate, LocalDate endDate, int numPeople, String neighborhood, double price) {
        return null;
    }

}
