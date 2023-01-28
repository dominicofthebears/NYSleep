package it.unipi.lsmsd.nysleep.business;

import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoAccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReservationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReviewDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoUserDAO;
import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.*;
import it.unipi.lsmsd.nysleep.server.AdminServicesRMI;
import org.bson.Document;
import org.neo4j.driver.Record;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AdminServices extends UserServices implements AdminServicesRMI {

    public AdminServices(){};

    public void modifyUser(AdminDTO oldAdminDTO, AdminDTO newAdminDTO) throws BusinessException, RemoteException {
        try{
            documentUserDAO = new MongoUserDAO();
            if(!oldAdminDTO.getEmail().equals(newAdminDTO.getEmail()) && documentUserDAO.checkEmail(newAdminDTO.getEmail())){
                throw new BusinessException("Email already in use");
            }
            Admin oldAdmin = new Admin();
            oldAdmin.setId(oldAdminDTO.getId());

            Admin newAdmin = new Admin();
            newAdmin.setId(oldAdmin.getId());
            newAdmin.setFirstName(newAdminDTO.getFirstName());
            newAdmin.setLastName(newAdminDTO.getLastName());
            newAdmin.setEmail(newAdminDTO.getEmail());
            newAdmin.setPassword(newAdminDTO.getPassword());
            newAdmin.setType("admin");
            newAdmin.setTitle(newAdminDTO.getTitle());
            documentUserDAO.modifyAccountInfo(oldAdmin, newAdmin);
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentUserDAO.closeConnection();
        }
    }
    public void removeAccommodation(AccommodationDTO accDTO) throws BusinessException, RemoteException {
        try{
            documentAccDAO = new MongoAccommodationDAO();
            documentRevDAO = new MongoReviewDAO();
            documentResDAO = new MongoReservationDAO();
            Accommodation acc = new Accommodation();
            acc.setId(accDTO.getId());
            documentAccDAO.startTransaction();

            documentAccDAO.deleteAccommodation(acc);
            //cancella reviews dell'acc
            documentRevDAO.deleteAccReview(acc);
            //cancella reservations dell'acc
            documentResDAO.deleteAccReservation(acc);

            graphAccDAO.deleteAccommodation(acc);
            documentAccDAO.commitTransaction();
            documentRevDAO.commitTransaction();
            documentResDAO.commitTransaction();
        }catch(Exception e){
            documentAccDAO.abortTransaction();
            throw new BusinessException(e);
        }finally{
            documentAccDAO.closeConnection();
            documentRevDAO.closeConnection();
            documentResDAO.closeConnection();
        }
    }
    public PageDTO<ReservationDTO> showAccReservations(AccommodationDTO accDTO) throws BusinessException, RemoteException {
        try{
            documentResDAO = new MongoReservationDAO();
            Accommodation acc = new Accommodation();
            acc.setId(accDTO.getId());
            LinkedList<Document> docs = (LinkedList<Document>) documentResDAO.getAccReservations(acc);
            List<ReservationDTO> accReservations = new LinkedList<ReservationDTO>();
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
                        (Integer) resDoc.get("_id"), startDateCasted,
                        endDateCasted,
                        (double) resDoc.get("cost"),
                        (int) customerDoc.get("id"),
                        (String) customerDoc.get("first_name"),
                        (String) customerDoc.get("last_name"),
                        (String) customerDoc.get("country"),
                        (int) accommodationDoc.get("id"),
                        (String) accommodationDoc.get("name"),
                        (String) accommodationDoc.get("neighborhood"));
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

    public void deleteReview(CustomerReviewDTO customerReviewDTO, AccReviewDTO accReviewDTO) throws BusinessException, RemoteException {
        try{
            documentRevDAO = new MongoReviewDAO();
            documentAccDAO = new MongoAccommodationDAO();
            Review review = new Review();
            review.setId(customerReviewDTO.getId());
            Customer cus = new Customer();
            cus.setId(accReviewDTO.getCustomerId());
            Accommodation acc = new Accommodation();
            acc.setId(customerReviewDTO.getAccommodationId());
            review.setCustomer(cus);
            review.setAccommodation(acc);
            documentRevDAO.startTransaction();

            documentRevDAO.deleteReview(review);
            graphRevDAO.deleteReview(review);
            documentAccDAO.decreaseNumReview(review.getAccommodation());

            documentRevDAO.commitTransaction();
            documentAccDAO.commitTransaction();
        }catch(Exception e){
            documentRevDAO.abortTransaction();
            throw new BusinessException(e);
        }finally{
            documentRevDAO.closeConnection();
            documentAccDAO.closeConnection();
        }
    }

    public void deleteReservation(ReservationDTO reservationDTO) throws BusinessException, RemoteException {
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();
            Reservation reservation = new Reservation();
            reservation.setId(reservationDTO.getId());
            reservation.setStartDate(reservationDTO.getstartDate());
            reservation.setEndDate(reservationDTO.getendDate());
            documentResDAO.startTransaction();

            documentResDAO.deleteReservation(reservation);
            documentAccDAO.deleteReservation(reservation.getAccommodation(),reservation);

            documentResDAO.commitTransaction();
        }catch(Exception e){
            throw new BusinessException(e);
        }finally {
            documentResDAO.closeConnection();
            documentAccDAO.closeConnection();
        }
    }
    
    public Document customerWhoHasSpentTheMost() throws BusinessException, RemoteException{
        try{
            documentResDAO = new MongoReservationDAO();
            Document doc = documentResDAO.custWhoHasSpentTheMost();
            return doc;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentResDAO.closeConnection();
        }
    }

    public List<Document> mostReservedAccommodationForEachNeighborhood() throws BusinessException, RemoteException{
        try{
            documentResDAO = new MongoReservationDAO();
            List<Document> docs = documentResDAO.mostResAccForEachNeigh();
            return docs;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentResDAO.closeConnection();
        }
    }

    public Document customerWithHighestAverageExpense() throws BusinessException, RemoteException{
        try{
            documentResDAO = new MongoReservationDAO();
            Document doc = documentResDAO.custWithHighestAvgExpense();
            return doc;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentResDAO.closeConnection();
        }
    }

    public List<Document> mostReservingCountryForNeighborhood() throws BusinessException, RemoteException{
        try{
            documentResDAO = new MongoReservationDAO();
            List<Document> docs = documentResDAO.mostResCountryForNeigh();
            return docs;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentResDAO.closeConnection();
        }
    }

    public List<Document> mostAndLeastExpensiveAccommodationForPropertyType() throws BusinessException, RemoteException{
        try{
            documentAccDAO = new MongoAccommodationDAO();
            List<Document> docs = documentAccDAO.mostExpensiveAndLeastExpensiveAccommodationForPropertyType();
            return docs;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentAccDAO.closeConnection();
        }
    }

    public List<Document> averageRatingByCountry() throws BusinessException, RemoteException{
        try{
            documentRevDAO = new MongoReviewDAO();
            List<Document> docs = documentRevDAO.avgRatingByCountry();
            return docs;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally {
            documentRevDAO.closeConnection();
        }
    }

    public Record mostActiveUser() throws BusinessException, RemoteException{
        try{
            Record record = graphCustomerDAO.mostActiveUser();
            return record;
        }catch (Exception e){
            throw new BusinessException(e);
        }
    }

    public Record renterWithMostAccommodation() throws BusinessException, RemoteException{
        try{
            Record record = graphRenterDAO.renterWithMostAccommodation();
            return record;
        }catch (Exception e){
            throw new BusinessException(e);
        }
    }

    public Record bestReviewedRenter(int min) throws BusinessException, RemoteException{
        try{
            Record record = graphRenterDAO.bestReviewedRenter(min);
            return record;
        }catch (Exception e){
            throw new BusinessException(e);
        }
    }

    public Record renterWithMostAccommodationForNeighborhood(String neighborhood) throws BusinessException, RemoteException{
        try{
            Record record = graphRenterDAO.renterWithMostAccommodationForNeighborhood(neighborhood);
            return record;
        }catch (Exception e){
            throw new BusinessException(e);
        }
    }
}
