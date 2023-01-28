package it.unipi.lsmsd.nysleep.business;

import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoAccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReservationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReviewDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoUserDAO;
import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.*;

import it.unipi.lsmsd.nysleep.server.RenterServicesRMI;
import org.bson.Document;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RenterServices extends UserServices implements RenterServicesRMI {

    public RenterServices(){}

    public void modifyUser(ModifiedRenterDTO oldRenterDTO, ModifiedRenterDTO newRenterDTO)
            throws BusinessException, RemoteException {
        Renter oldRenter = new Renter();
        Renter newRenter = new Renter();
        try{
        documentUserDAO = new MongoUserDAO();

        if(!oldRenterDTO.getEmail().equals(newRenterDTO.getEmail()) && documentUserDAO.checkEmail(newRenterDTO.getEmail())){
            throw new BusinessException("Email already in use");
        }

        oldRenter.setId(oldRenterDTO.getId());
        /*oldRenter.setFirstName(oldRenterDTO.getFirstName());
        oldRenter.setLastName(oldRenterDTO.getLastName());
        oldRenter.setPhoneNumber(oldRenterDTO.getPhone());
        oldRenter.setWorkEmail(oldRenterDTO.getWorkEmail());
        oldRenter.setPassword(oldRenterDTO.getPassword());
        oldRenter.setEmail(oldRenterDTO.getEmail());
        oldRenter.setType("renter");
        oldRenter.setUrl_prof_pic(oldRenterDTO.getUrl_profile_pic());*/

        newRenter.setId(oldRenterDTO.getId());
        newRenter.setFirstName(newRenterDTO.getFirstName());
        newRenter.setLastName(newRenterDTO.getLastName());
        newRenter.setPhoneNumber(newRenterDTO.getPhone());
        newRenter.setWorkEmail(newRenterDTO.getWorkEmail());
        newRenter.setPassword(newRenterDTO.getPassword());
        newRenter.setEmail(newRenterDTO.getEmail());
        newRenter.setType("renter");
        newRenter.setUrl_prof_pic(newRenterDTO.getUrl_profile_pic());

        newRenter.setId(oldRenter.getId());
        if(!oldRenterDTO.getFirstName().equals(newRenterDTO.getFirstName())||
           !oldRenterDTO.getLastName().equals(newRenterDTO.getLastName())||
           !oldRenterDTO.getWorkEmail().equals(newRenterDTO.getWorkEmail())||
           !oldRenterDTO.getPhone().equals(newRenterDTO.getPhone()))
        {
            documentAccDAO = new MongoAccommodationDAO();
            LinkedList<Document> accDocs = (LinkedList<Document>) documentAccDAO.getSearchedAcc(newRenter);
            for(Document doc: accDocs){
                Accommodation acc = new Accommodation();
                acc.setId((int)doc.get("_id"));
                acc.setRenter(newRenter);
                documentAccDAO.updateAccommodationRenter(acc);
            }
        }
        documentUserDAO.startTransaction();
        documentUserDAO.modifyAccountInfo(oldRenter,newRenter);
        graphRenterDAO.modifyAccountInfo(oldRenter,newRenter);
        documentUserDAO.commitTransaction();
        }catch(Exception e){
            documentUserDAO.abortTransaction();
            throw new BusinessException(e);
        }finally {
            documentUserDAO.closeConnection();
        }
    }

    public void addAccommodation(AccommodationDetailsDTO accDTO) throws BusinessException, RemoteException {
        try{

            documentAccDAO = new MongoAccommodationDAO();
            Accommodation acc = new Accommodation();
            Renter rent = new Renter();

            rent.setId(accDTO.getRenterDetailsDTO().getId());
            rent.setFirstName(accDTO.getRenterDetailsDTO().getFirstName());
            rent.setLastName(accDTO.getRenterDetailsDTO().getLastName());
            rent.setWorkEmail(accDTO.getRenterDetailsDTO().getWorkEmail());
            rent.setPhoneNumber(accDTO.getRenterDetailsDTO().getPhone());

            acc.setId(documentAccDAO.getLastId(documentAccDAO.getCollection()));
            acc.setName(accDTO.getName());
            acc.setNeighborhood(accDTO.getNeighborhood());
            acc.setNumBeds(accDTO.getNumBeds());
            acc.setRating(Double.NaN);
            acc.setNumReviews(0);
            acc.setPropertyType(accDTO.getPropertyType());
            acc.setNumRooms(accDTO.getNumRooms());
            acc.setAmenities(accDTO.getAmenities());
            acc.setPrice(accDTO.getPrice());
            acc.setRenter(rent);
            List<Reservation> l = new ArrayList<>();
            acc.setReservations(l);

            documentAccDAO.startTransaction();
            documentAccDAO.createAccommodation(acc);
            graphAccDAO.createAccommodation(acc);
            documentAccDAO.commitTransaction();
        }catch(Exception e){
            documentAccDAO.abortTransaction();
            throw new BusinessException(e);
        }
        finally{
            documentAccDAO.closeConnection();
        }
    }


    public void removeAccommodation(AccommodationDetailsDTO accDTO) throws BusinessException, RemoteException {
        Accommodation acc = new Accommodation();
        acc.setId(accDTO.getId());
        try{
            documentAccDAO = new MongoAccommodationDAO();
            documentRevDAO = new MongoReviewDAO();
            documentResDAO = new MongoReservationDAO();
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

    public void modifyAccommodation(ModifiedAccDTO oldAccDTO, ModifiedAccDTO newAccDTO) throws BusinessException, RemoteException {
        Accommodation oldAcc = new Accommodation();
        Accommodation newAcc = new Accommodation();

        try{

            documentAccDAO = new MongoAccommodationDAO();
            Renter rent = new Renter();

            oldAcc.setId(oldAccDTO.getId());
            oldAcc.setName(oldAccDTO.getName());
            oldAcc.setNeighborhood("");
            oldAcc.setNumBeds(oldAccDTO.getNumBeds());
            oldAcc.setRating(Double.NaN);
            oldAcc.setNumReviews(0);
            oldAcc.setPropertyType("");
            oldAcc.setNumRooms(oldAccDTO.getNumRooms());
            oldAcc.setAmenities(oldAccDTO.getAmenities());
            oldAcc.setPrice(oldAccDTO.getPrice());
            oldAcc.setRenter(rent);

            newAcc.setId(newAccDTO.getId());
            newAcc.setName(newAccDTO.getName());
            newAcc.setNeighborhood("");
            newAcc.setNumBeds(newAccDTO.getNumBeds());
            newAcc.setRating(Double.NaN);
            newAcc.setNumReviews(0);
            newAcc.setPropertyType("");
            newAcc.setNumRooms(newAccDTO.getNumRooms());
            newAcc.setAmenities(newAccDTO.getAmenities());
            newAcc.setPrice(newAccDTO.getPrice());
            newAcc.setRenter(rent);


            documentAccDAO.startTransaction();
            if(!oldAcc.getName().equals(newAcc.getName())){
                documentRevDAO = new MongoReviewDAO();
                LinkedList<Document> accDocs = (LinkedList<Document>) documentRevDAO.getReviewsForAcc(oldAcc);
                Customer cus = new Customer();
                for(Document doc: accDocs){
                    Review r = new Review();
                    r.setId((int) doc.get("_id"));
                    cus.setId((int) doc.get("customer.id"));
                    cus.setFirstName((String) doc.get("customer.first_name"));
                    cus.setLastName((String) doc.get("customer.last_name"));
                    cus.setCountry((String) doc.get("customer.country"));
                    Review newr = new Review((int)doc.get("_id"), newAcc, cus,
                            (String) doc.get("comment"), (int) doc.get("rate"), (LocalDate) doc.get("date"));
                    documentRevDAO.modifyReview(r, newr);
                }

                documentResDAO = new MongoReservationDAO();
                LinkedList<Document> resDocs = (LinkedList<Document>) documentResDAO.getAccReservations(oldAcc);
                for(Document doc: resDocs){
                    Reservation res = new Reservation();
                    res.setId((int) doc.get("_id"));
                    cus.setId((int) doc.get("customer.id"));
                    cus.setFirstName((String) doc.get("customer.first_name"));
                    cus.setLastName((String) doc.get("customer.last_name"));
                    cus.setCountry((String) doc.get("customer.country"));
                    Reservation newRes = new Reservation((int)doc.get("_id"), (LocalDate) doc.get("start_date"), (LocalDate) doc.get("end_date"),
                            (int) doc.get("cost"), cus, newAcc);
                    documentResDAO.modifyReservation(res, newRes);
                }
            }
            documentAccDAO.updateAccommodation(oldAcc, newAcc);
            graphAccDAO.updateAccommodation(oldAcc,newAcc);
            documentAccDAO.commitTransaction();
        }catch(Exception e){
            documentAccDAO.abortTransaction();
            throw new BusinessException(e);
        }finally{
            documentAccDAO.closeConnection();
        }
    }

    public PageDTO<ReservationDTO> showRenterReservation(RenterDTO renterDTO) throws BusinessException, RemoteException {
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();

            Renter renter = new Renter();
            renter.setId(renterDTO.getId());

            LinkedList<Document> accDocs = (LinkedList<Document>) documentAccDAO.getSearchedAcc(renter);
            List<ReservationDTO> resDTOList = new LinkedList<ReservationDTO>();
            Accommodation accTemp = new Accommodation();
            for (Document accDoc: accDocs){
                accTemp.setId(accDoc.getInteger("id"));
                for(Document resDoc :( ArrayList<Document>)  documentResDAO.getAccReservations(accTemp)){   //iterate all over the documents and extract reservation to put in the DTO
                    //Casting Date to LocalDate because mongoDB only return Date that is deprecated;
                    Date startDate = (Date) resDoc.get("start_date");
                    LocalDate startDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Date endDate =  (Date) resDoc.get("end_date");
                    LocalDate endDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    Document customerDoc = (Document) resDoc.get("customer");
                    Document accommodationDoc = (Document) resDoc.get("accommodation");

                    //Create DTO
                    ReservationDTO resDTO = new ReservationDTO((int) resDoc.get("_id"),
                            startDateCasted,
                            endDateCasted,
                            (Integer) resDoc.get("cost"),
                            (int) customerDoc.get("id"),
                            (String) customerDoc.get("first_name"),
                            (String) customerDoc.get("last_name"),
                            (String) customerDoc.get("country"),
                            (int) accommodationDoc.get("id"),
                            (String) accommodationDoc.get("name"),
                            (String) accommodationDoc.get("neighborhood"));
                    resDTOList.add(resDTO);
                }
            }
            PageDTO<ReservationDTO> resPage = new PageDTO<ReservationDTO>();
            resPage.setEntries(resDTOList);
            return resPage;
        }catch(Exception e){
            throw new BusinessException(e);
        }finally{
            documentResDAO.closeConnection();
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


    public PageDTO<AccommodationDTO> showRenterAccommodation(RenterDTO renterDTO) throws BusinessException, RemoteException {
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();

            Renter renter = new Renter();
            renter.setId(renterDTO.getId());

            LinkedList<Document> accDocs = (LinkedList<Document>) documentAccDAO.getSearchedAcc(renter);
            LinkedList<AccommodationDTO> accDTOList = new LinkedList<>();
            for (Document accDoc: accDocs){
                AccommodationDTO acc = new AccommodationDTO();
                acc.setId(accDoc.getInteger("_id"));
                acc.setName(accDoc.getString("name"));
                acc.setNeighborhood(accDoc.getString("neighborhood"));
                acc.setRating(accDoc.getDouble("rating"));
            }
            PageDTO<AccommodationDTO> accPage = new PageDTO<AccommodationDTO>();
            accPage.setEntries(accDTOList);
            return accPage;
        }catch(Exception e){
            throw new BusinessException(e);
        }finally{
            documentResDAO.closeConnection();
            documentAccDAO.closeConnection();
        }
    }

}

