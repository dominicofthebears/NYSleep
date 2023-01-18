package nysleep.DAO.mongoDB;

import nysleep.DAO.ReservationDAO;
import nysleep.DAO.base.MongoBaseDAO;

import nysleep.DTO.PageDTO;
import nysleep.DTO.ReservationDTO;

import nysleep.model.Accommodation;
import nysleep.model.Customer;
import nysleep.model.Reservation;

import org.bson.Document;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

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
}
