package nysleep.business;

import nysleep.DAO.mongoDB.*;
import nysleep.DAO.neo4jDB.NeoCustomerDAO;
import nysleep.DAO.neo4jDB.NeoReviewDAO;
import nysleep.DTO.AccommodationDetailsDTO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.RenterDetailsDTO;
import nysleep.DTO.ReservationDTO;
import nysleep.business.exception.BusinessException;
import nysleep.model.*;

import org.bson.Document;
import org.w3c.dom.traversal.DocumentTraversal;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerServices extends UserServices{

    public CustomerServices(){}

    //Get the renterDTO's of an accommodation
    public RenterDetailsDTO getAccRenter(Accommodation acc) throws BusinessException {
        try {
            documentUserDAO = new MongoUserDAO();
            Renter renter = (Renter) documentUserDAO.getUser(acc.getRenter());
            RenterDetailsDTO renterDTO = new RenterDetailsDTO(renter.getFirstName()
                    , renter.getLastName()
                    , renter.getWorkEmail()
                    , renter.getPhone()
                    , renter.getUrl_prof_pic());
            return renterDTO;
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            documentUserDAO.closeConnection();
        }
    }

    public void insertReview(Review review) throws BusinessException {
        try {
            documentRevDAO = new MongoReviewDAO();
            documentAccDAO = new MongoAccommodationDAO();

            documentRevDAO.createReview(review);
            graphRevDAO.createReview(review);
            documentAccDAO.incrementNumReview(review.getAccommodation());
        }catch(Exception e) {
            throw new BusinessException(e);
        }
        finally{
            documentRevDAO.closeConnection();
            documentAccDAO.closeConnection();
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


    public void insertReservation(Reservation reservation) throws BusinessException {
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();

            documentResDAO.createReservation(reservation);
            documentAccDAO.insertReservation(reservation.getAccommodation(),reservation);
            documentAccDAO.incrementNumReview(reservation.getAccommodation());
        }catch(Exception e){
            throw new BusinessException(e);}
        finally{
            documentResDAO.closeConnection();
            documentAccDAO.closeConnection();
        }
    }


    public  PageDTO<ReservationDTO> viewReservations(Customer customer) throws BusinessException {
        try{
            documentResDAO = new MongoReservationDAO();
            ArrayList<Document>docs =  (ArrayList<Document>) documentResDAO.getCustomerReservations(customer);
            List<ReservationDTO> resDTOList = new ArrayList<ReservationDTO>();
            for(Document doc: docs){        //iterate all over the documents and extract reservation to put in the DTO

                //Casting Date to LocalDate because mongoDB only return Date that is deprecated;
                Date startDate = (Date) doc.get("start_date");
                LocalDate startDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Date endDate =  (Date) doc.get("end_date");
                LocalDate endDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                org.bson.Document customerDoc = (org.bson.Document) doc.get("customer");
                org.bson.Document accommodationDoc = (org.bson.Document) doc.get("accommodation");
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
            PageDTO<ReservationDTO> resPage = new PageDTO<>();
            resPage.setEntries(resDTOList);
            return resPage;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally{documentResDAO.closeConnection();}
    }

    public void modifyUser(RegisteredUser oldUser,RegisteredUser newUser) throws BusinessException {
        try{

            documentUserDAO = new MongoUserDAO();
            graphCustomerDAO = new NeoCustomerDAO();
            newUser.setId(oldUser.getId());
            documentUserDAO.startTransaction();
            documentUserDAO.modifyAccountInfo(oldUser,newUser);
            documentUserDAO.commitTransaction();
        }catch(Exception e){
            documentUserDAO.closeConnection();
            throw new BusinessException(e);
        }
        try{
            graphCustomerDAO.modifyAccountInfo(oldUser, newUser);
        }catch (Exception e){
            documentUserDAO.abortTransaction();
            documentUserDAO.closeConnection();
            throw new BusinessException(e);
        }
    }
}
