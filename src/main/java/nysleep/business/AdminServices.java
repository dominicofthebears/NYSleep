package nysleep.business;

import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoReservationDAO;
import nysleep.DAO.mongoDB.MongoReviewDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.DAO.neo4jDB.NeoAccommodationDAO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.ReservationDTO;
import nysleep.business.exception.BusinessException;
import nysleep.model.*;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminServices extends UserServices{

    public AdminServices(){};

    public void modifyUser(Admin oldAdmin, Admin newAdmin) throws BusinessException {
        try{
            documentUserDAO = new MongoUserDAO();
            documentUserDAO.modifyAccountInfo(oldAdmin, newAdmin);
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentUserDAO.closeConnection();
        }
    }
    public void removeAccommodation(Accommodation acc) throws BusinessException {
        try{
            documentAccDAO = new MongoAccommodationDAO();
            graphAccDAO = new NeoAccommodationDAO();
            documentAccDAO.deleteAccommodation(acc);

            //cancella reviews dell'acc
            documentRevDAO.deleteAccReview(acc);

            //cancella reservations dell'acc
            documentResDAO.deleteAccReservation(acc);
            graphAccDAO.deleteAccommodation(acc);

        }catch(Exception e){
            throw new BusinessException(e);
        }finally{
            documentAccDAO.closeConnection();
        }
    }

    public PageDTO<ReservationDTO> showAccReservations(Accommodation acc) throws BusinessException {
        try{
            documentResDAO = new MongoReservationDAO();
            ArrayList<Document> docs = (ArrayList<Document>) documentResDAO.getAccReservations(acc);
            List<ReservationDTO> accReservations = new ArrayList<ReservationDTO>();
            for(Document resDoc : docs){   //iterate all over the documents and extract reservation to put in the DTO
                //Casting Date to LocalDate because mongoDB only return Date that is deprecated;
                Date startDate = (Date) resDoc.get("start_date");
                LocalDate startDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Date endDate =  (Date) resDoc.get("end_date");
                LocalDate endDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Document customerDoc = (Document) resDoc.get("customer");
                Document accommodationDoc = (Document) resDoc.get("accommodation");

                //Create DTO
                ReservationDTO resDTO = new ReservationDTO(
                        startDateCasted,
                        endDateCasted,
                        (Integer) resDoc.get("cost"),
                        (int) customerDoc.get("id"),
                        (String) customerDoc.get("first_name"),
                        (String) customerDoc.get("last_name"),
                        (int) accommodationDoc.get("id"),
                        (String) accommodationDoc.get("name")
                );
                accReservations.add(resDTO);
            }
            PageDTO<ReservationDTO> resPage = new PageDTO<ReservationDTO>();
            resPage.setEntries(accReservations);
            return resPage;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentResDAO.closeConnection();
        }
    }

    public void deleteReview(Review review) throws BusinessException {
        try{
            documentRevDAO = new MongoReviewDAO();
            documentAccDAO = new MongoAccommodationDAO();

            documentRevDAO.deleteReview(review);
            graphRevDAO.deleteReview(review);
            documentAccDAO.decreaseNumReview(review.getAccommodation());

        }catch(Exception e){
            throw new BusinessException(e);
        }finally{
            documentRevDAO.closeConnection();
            documentAccDAO.closeConnection();
        }
    }
}
