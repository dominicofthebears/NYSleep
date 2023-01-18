package nysleep.business;

import nysleep.DAO.mongoDB.*;
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
        RenterDetailsDTO renterDTO = null;
        try {
            documentUserDAO = new MongoUserDAO();
            Renter renter = (Renter) documentUserDAO.getUser(acc.getRenter());
            renterDTO = new RenterDetailsDTO(renter.getFirstName()
                    , renter.getLastName()
                    , renter.getWorkEmail()
                    , renter.getPhone()
                    , renter.getUrl_prof_pic());

        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            documentUserDAO.closeConnection();
            return renterDTO;
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
            documentAccDAO.closeConnection());
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
        }
    }


    public void insertReservation(Reservation reservation){
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();

            documentResDAO.createReservation(reservation);
            documentAccDAO.insertReservation(reservation.getAccommodation(),reservation);
            documentAccDAO.incrementNumReview(reservation.getAccommodation());
        }catch(Exception e){e.printStackTrace();}
        finally{
            documentResDAO.closeConnection();
            documentAccDAO.closeConnection();
        }
    }



    public  PageDTO<ReservationDTO> viewReservations(Customer customer){
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
            PageDTO<ReservationDTO> resPage = new PageDTO<ReservationDTO>();
            resPage.setEntries(resDTOList);
            return resPage;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally{documentResDAO.closeConnection();}
    }

    public void modifyUser(RegisteredUser oldUser,RegisteredUser newUser){
        try{

            documentUserDAO = new MongoUserDAO();
            documentUserDAO.modifyAccountInfo(oldUser,newUser);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            documentUserDAO.closeConnection();
        }
    }
}
