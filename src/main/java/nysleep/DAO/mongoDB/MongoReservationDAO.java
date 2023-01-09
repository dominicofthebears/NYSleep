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


    public PageDTO<ReservationDTO> getCustomerReservations(Customer customer) {

        Document searchQuery = new Document("customer.id",new Document("$eq",customer.getId()));
        ArrayList<Document> docs = readDoc(searchQuery,COLLECTION);
        List<ReservationDTO> resDTOList = new ArrayList<ReservationDTO>();

        for(Document doc: docs){        //iterate all over the documents and extract reservation to put in the DTO

            //Casting Date to LocalDate because mongoDB only return Date that is deprecated;
            Date startDate = (Date) doc.get("start_date");
            LocalDate startDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Date endDate =  (Date) doc.get("end_date");
            LocalDate endDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Document customerDoc = (Document) doc.get("customer");
            Document accommodationDoc = (Document) doc.get("accommodation");
            //Create DTO
            ReservationDTO resDTO = new ReservationDTO(
                    startDateCasted,
                    endDateCasted,
                    (Integer) doc.get("cost"),
                    (int) customerDoc.get("id"),
                    (String) customerDoc.get("first_name"),
                    (String) customerDoc.get("last_name"),
                    (int) accommodationDoc.get("id"),
                    (String) accommodationDoc.get("name")
                    );
            resDTOList.add(resDTO);
        }

        PageDTO<ReservationDTO> resPage = new PageDTO<ReservationDTO>();
        resPage.setEntries(resDTOList);
        return resPage;
    }

    public PageDTO<ReservationDTO> getAccReservations(Accommodation acc){

        Document searchQuery = new Document("accommodation.id",new Document("$eq",acc.getId()));
        ArrayList<Document> docs = readDoc(searchQuery,COLLECTION);
        List<ReservationDTO> resDTOList = new ArrayList<ReservationDTO>();

        for(Document doc: docs){        //iterate all over the documents and extract reservation to put in the DTO

            //Casting Date to LocalDate because mongoDB only return Date that is deprecated;
            Date startDate = (Date) doc.get("start_date");
            LocalDate startDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Date endDate =  (Date) doc.get("end_date");
            LocalDate endDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Document customerDoc = (Document) doc.get("customer");
            Document accommodationDoc = (Document) doc.get("accommodation");
            //Create DTO
            ReservationDTO resDTO = new ReservationDTO(
                    startDateCasted,
                    endDateCasted,
                    (Integer) doc.get("cost"),
                    (int) customerDoc.get("id"),
                    (String) customerDoc.get("first_name"),
                    (String) customerDoc.get("last_name"),
                    (int) accommodationDoc.get("id"),
                    (String) accommodationDoc.get("name")
            );
            resDTOList.add(resDTO);
        }

        PageDTO<ReservationDTO> resPage = new PageDTO<ReservationDTO>();
        resPage.setEntries(resDTOList);
        return resPage;
    };
}
