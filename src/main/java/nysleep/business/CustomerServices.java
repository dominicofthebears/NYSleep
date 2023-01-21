package nysleep.business;

import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoReservationDAO;
import nysleep.DAO.mongoDB.MongoReviewDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.DAO.neo4jDB.NeoCustomerDAO;
import nysleep.DAO.neo4jDB.NeoReviewDAO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.RenterDetailsDTO;
import nysleep.DTO.ReservationDTO;
import nysleep.business.exception.BusinessException;
import nysleep.model.*;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
            graphRevDAO = new NeoReviewDAO();

            review.setId(documentRevDAO.getLastId(documentRevDAO.getCollection()));
            documentRevDAO.startTransaction();
            documentAccDAO.startTransaction();

            documentRevDAO.createReview(review);
            documentAccDAO.incrementNumReview(review.getAccommodation());

            graphRevDAO.createReview(review);

            documentRevDAO.commitTransaction();  //IF commit will fail MongoDB driver will retry the commit operation one time
            documentAccDAO.commitTransaction();

        }catch(Exception e) {
            documentRevDAO.abortTransaction();
            documentAccDAO.abortTransaction();
            throw new BusinessException(e);
        }finally{
            documentRevDAO.closeConnection();
            documentAccDAO.closeConnection();
        }
    }


    public void deleteReview(Review review) throws BusinessException {
        try{
            documentRevDAO = new MongoReviewDAO();
            documentAccDAO = new MongoAccommodationDAO();

            documentRevDAO.startTransaction();
            documentAccDAO.startTransaction();

            documentRevDAO.deleteReview(review);
            graphRevDAO.deleteReview(review);
            documentAccDAO.decreaseNumReview(review.getAccommodation());

            documentRevDAO.commitTransaction();
            documentAccDAO.commitTransaction();
        }catch(Exception e){
            documentRevDAO.abortTransaction();
            documentAccDAO.abortTransaction();
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

            reservation.setId(documentResDAO.getLastId(documentResDAO.getCollection()));
            documentResDAO.createReservation(reservation);
            documentAccDAO.insertReservation(reservation.getAccommodation(),reservation);

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
            LinkedList<Document> docs =  (LinkedList<Document>) documentResDAO.getCustomerReservations(customer);
            List<ReservationDTO> resDTOList = new LinkedList<ReservationDTO>();
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

            documentUserDAO.startTransaction();

            newUser.setId(oldUser.getId());
            documentUserDAO.startTransaction();
            documentUserDAO.modifyAccountInfo(oldUser,newUser);
            graphCustomerDAO.modifyAccountInfo(oldUser,newUser);
            documentUserDAO.commitTransaction();
        }catch(Exception e){
            documentUserDAO.abortTransaction();
            throw new BusinessException(e);
        }finally{
            documentUserDAO.closeConnection();
        }
    }
}
