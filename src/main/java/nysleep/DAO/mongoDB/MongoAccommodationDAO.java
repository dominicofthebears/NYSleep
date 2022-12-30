package nysleep.DAO.mongoDB;

import com.mongodb.client.MongoCollection;
import nysleep.DAO.AccommodationDAO;
import nysleep.DTO.AccommodationDTO;
import nysleep.DTO.PageDTO;

import nysleep.model.Accommodation;
import nysleep.model.Renter;

import nysleep.DAO.base.MongoBaseDAO;
import org.bson.Document;

import javax.swing.event.DocumentEvent;
import java.time.LocalDate;
import java.util.Collection;

public class MongoAccommodationDAO extends MongoBaseDAO implements AccommodationDAO {
private final String COLLECTION = "accommodation";

    private static Document toDoc(Accommodation acc){
        Document doc = new Document("id",acc.getId())
                            .append("name",acc.getName())
                            .append("neighborhood",acc.getNeighborhood())
                            .append("images_URL",acc.getImagesURL())
                            .append("num_beds",acc.getNumBeds())
                            .append("num_rooms",acc.getNumRooms())
                            .append("price",acc.getPrice())
                            .append("num_reviews",acc.getNumReviews())
                            .append("property_type",acc.getPropertyType())
                            .append("amenities",acc.getAmenities())
                            .append("rating",acc.getRating());
                            ;
    return doc;
    }

    @Override
    public void createAccommodation(Accommodation acc) {
        MongoCollection collection = MongoBaseDAO.connect(dbName,COLLECTION);
        Document doc = toDoc(acc);
        collection.insertOne(doc);
    }

    @Override
    public void deleteAccommodation(Accommodation acc) {

    }

    @Override
    public void updateRating(Accommodation acc, int rating) {

    }

    @Override
    public void updateAccommodation(Accommodation oldACc, Accommodation newAcc) {

    }

    @Override
    public PageDTO<AccommodationDTO> getAccHomePage() {
        return null;
    }

    @Override
    public AccommodationDTO getAccommodation(Accommodation acc) {
        return null;
    }

    @Override
    public PageDTO<AccommodationDTO> getSearchedAcc(LocalDate startDate, LocalDate endDate, int numPeople, String neighborhood, float price) {
        return null;
    }

    @Override
    public PageDTO<AccommodationDTO> getRenterAccommodations(Renter renter) {
        return null;
    }

    @Override
    public PageDTO<AccommodationDTO> viewAccommodationHomePage() {
        return null;
    }

    @Override
    public AccommodationDTO viewAccommodation(Accommodation acc) {
        return null;
    }
}
