package nysleep.DAO.mongoDB;

import nysleep.DAO.ReservationDAO;

import nysleep.DAO.base.MongoBaseDAO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.ReservationDTO;

import nysleep.model.Accommodation;
import nysleep.model.Customer;
import nysleep.model.Renter;
import nysleep.model.Reservation;

import org.bson.Document;

public class MongoReservationDAO extends MongoBaseDAO implements ReservationDAO {
    private final String COLLECTION = "Reservations";

    private static Document toDoc(Reservation res){
        Document customerDoc = new Document("_id",res.getCustomer().getId())
                .append("first_name",res.getCustomer().getFirstName())
                .append("last_name",res.getCustomer().getLastName())
                .append("country",res.getCustomer().getCountry());

        Document accDoc = new Document("_id",res.getAccommodation().getId())
                .append("name",res.getAccommodation().getName())
                .append("neighborhood",res.getAccommodation().getNeighborhood());

        Document doc = new Document("Customer",customerDoc)
                .append("Accommodation",accDoc)
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

    @Override
    public PageDTO<ReservationDTO> getRenterReservations(Renter renter) {
        return null;
    }

    @Override
    public PageDTO<ReservationDTO> getCustomerReservations(Customer customer) {
        return null;
    }

    PageDTO<ReservationDTO> getAccReservations(Accommodation acc){
        return null;
    };
}
