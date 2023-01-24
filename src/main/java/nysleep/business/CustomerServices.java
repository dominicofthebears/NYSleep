package nysleep.business;

import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoReservationDAO;
import nysleep.DAO.mongoDB.MongoReviewDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.DAO.neo4jDB.NeoCustomerDAO;
import nysleep.DAO.neo4jDB.NeoReviewDAO;
import nysleep.DTO.*;
import nysleep.business.exception.BusinessException;
import nysleep.model.*;
import org.bson.Document;
import org.neo4j.driver.Record;

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
            double rate=graphAccDAO.recomputeRate(review.getAccommodation());

            documentAccDAO.updateRating(review.getAccommodation(), rate);
            graphAccDAO.updateRating(review.getAccommodation(), rate);

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

            double rate=graphAccDAO.recomputeRate(review.getAccommodation());

            documentAccDAO.updateRating(review.getAccommodation(), rate);
            graphAccDAO.updateRating(review.getAccommodation(), rate);

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
            documentAccDAO.cleanReservations(reservation.getAccommodation());
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

    public PageDTO<AccommodationDTO> showSuggestedAccommodations(Customer customer) throws BusinessException{
        try{
            LinkedList<Record> accommodations=(LinkedList<Record>) graphAccDAO.showSuggestedAccommodation(customer);
            List<AccommodationDTO> accDTOList = new LinkedList<AccommodationDTO>();
            for(Record rec : accommodations){
                AccommodationDTO acc = new AccommodationDTO();
                acc.setId(rec.get("id").asInt());
                acc.setName(rec.get("name").asString());
                acc.setNeighborhood(rec.get("neighborhood").asString());
                if(rec.get("rating").isNull())
                    acc.setRating(Double.NaN);
                else
                    acc.setRating(rec.get("rating").asDouble());
                accDTOList.add(acc);
            }
            PageDTO<AccommodationDTO> resPage = new PageDTO<>();
            resPage.setEntries(accDTOList);
            return resPage;
        }catch(Exception e){
            throw new BusinessException(e);
        }
    }

    //shows the accommodations owned by a renter who the customer rated 4 or more
    public PageDTO<AccommodationDTO> showAccommodationOfSuggestedRenter(Customer customer) throws BusinessException{
        try{
            LinkedList<Record> accommodations=(LinkedList<Record>) graphAccDAO.showAccommodationOfLikedRenter(customer);
            List<AccommodationDTO> accDTOList = new LinkedList<AccommodationDTO>();
            for(Record rec : accommodations){
                AccommodationDTO acc = new AccommodationDTO();
                acc.setId(rec.get("id").asInt());
                acc.setName(rec.get("name").asString());
                acc.setNeighborhood(rec.get("neighborhood").asString());
                if(rec.get("rating").isNull())
                    acc.setRating(Double.NaN);
                else
                    acc.setRating(rec.get("rating").asDouble());
                accDTOList.add(acc);
            }
            PageDTO<AccommodationDTO> resPage = new PageDTO<>();
            resPage.setEntries(accDTOList);
            return resPage;
        }catch(Exception e){
            throw new BusinessException(e);
        }
    }

    public PageDTO<CustomerReviewDTO> getOwnReviews(Customer customer){
        ArrayList<CustomerReviewDTO> customerReviewDTOList = new ArrayList<CustomerReviewDTO>();
        documentRevDAO = new MongoReviewDAO();
        List<Document> docs = documentRevDAO.getReviewsForCustomer(customer);
        for(Document doc: docs){

            Document accommodationDoc = (Document) doc.get("accommodation");

            CustomerReviewDTO customerReviewDTO = new CustomerReviewDTO(
                    (int) accommodationDoc.get("id")
                    ,(String)accommodationDoc.get("name")
                    ,(int) doc.get("rate")
                    ,(String) doc.get("comment")
            );

            customerReviewDTOList.add(customerReviewDTO);
        }

        PageDTO<CustomerReviewDTO> pageDTO = new PageDTO<CustomerReviewDTO>();
        pageDTO.setEntries(customerReviewDTOList);

        return pageDTO;

    }

    public void deleteReservation(Reservation reservation) throws BusinessException {
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();
            documentResDAO.startTransaction();
            documentAccDAO.startTransaction();

            documentResDAO.deleteReservation(reservation);
            documentAccDAO.deleteReservation(reservation.getAccommodation(),reservation);

        }catch(Exception e){
            throw new BusinessException(e);
        }finally {
            documentResDAO.closeConnection();
            documentAccDAO.closeConnection();
        }
    }
}
