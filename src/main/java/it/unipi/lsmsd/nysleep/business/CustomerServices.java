package it.unipi.lsmsd.nysleep.business;

import it.unipi.lsmsd.nysleep.DAO.neo4jDB.NeoReviewDAO;
import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoAccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReservationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReviewDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoUserDAO;
import it.unipi.lsmsd.nysleep.business.RMI.CustomerServicesRMI;
import it.unipi.lsmsd.nysleep.model.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import org.bson.Document;
import org.neo4j.driver.Record;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CustomerServices extends UserServices implements CustomerServicesRMI {

    public CustomerServices(){
    }

    public void insertReview(AccReviewDTO accReviewDTO, CustomerReviewDTO customerReviewDTO) throws BusinessException, RemoteException {
        try {
            documentRevDAO = new MongoReviewDAO();
            documentAccDAO = new MongoAccommodationDAO();
            graphRevDAO = new NeoReviewDAO();

            Review review = new Review();
            review.setId(documentRevDAO.getLastId(documentRevDAO.getCollection()));
            Accommodation acc = new Accommodation();
            acc.setId(customerReviewDTO.getAccommodationId());
            acc.setName(customerReviewDTO.getAccommodationName());
            review.setAccommodation(acc);

            Customer cus = new Customer();
            cus.setId(accReviewDTO.getCustomerId());
            cus.setFirstName(accReviewDTO.getCustomerFirstName());
            cus.setLastName(accReviewDTO.getCustomerLastName());
            cus.setCountry(accReviewDTO.getCustomerCountry());
            review.setCustomer(cus);
            review.setComment(accReviewDTO.getComment());
            review.setRate(accReviewDTO.getRate());
            review.setDate(LocalDate.now());
            documentRevDAO.startTransaction();

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
            throw new BusinessException(e);
        }finally{
            documentRevDAO.closeConnection();
            documentAccDAO.closeConnection();
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

            double rate=graphAccDAO.recomputeRate(review.getAccommodation());

            documentAccDAO.updateRating(review.getAccommodation(), rate);
            graphAccDAO.updateRating(review.getAccommodation(), rate);

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


    public void insertReservation(ReservationDTO reservationDTO) throws BusinessException, RemoteException {
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();

            Reservation reservation = new Reservation();
            reservation.setId(documentResDAO.getLastId(documentResDAO.getCollection()));
            System.out.println("id: "+ reservation.getId());
            reservation.setStartDate(reservationDTO.getstartDate());
            reservation.setEndDate(reservationDTO.getendDate());
            reservation.setTotalCost(reservationDTO.getTotalCost());


            Customer customer = new Customer();
            customer.setId(reservationDTO.getCustomerId());
            customer.setFirstName(reservationDTO.getCustomerFirstName());
            customer.setLastName(reservationDTO.getCustomerLastName());
            customer.setCountry(reservationDTO.getCustomerCountry());
            reservation.setCustomer(customer);


            Accommodation accommodation=new Accommodation();
            accommodation.setId(reservationDTO.getAccommodationId());
            accommodation.setName(reservationDTO.getAccommodationName());
            accommodation.setNeighborhood(reservationDTO.getAccommodationNeighborhood());
            reservation.setAccommodation(accommodation);


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


    public PageDTO<ReservationDTO> viewReservations(CustomerDTO customerDTO) throws BusinessException, RemoteException {
        try{
            documentResDAO = new MongoReservationDAO();
            Customer customer = new Customer();
            customer.setId(customerDTO.getId());
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
                        (int) doc.get("_id"), startDateCasted,
                        endDateCasted,
                        (double) doc.get("cost"),
                        (int) customerDoc.get("id"),
                        (String) customerDoc.get("first_name"),
                        (String) customerDoc.get("last_name"),
                        (String) customerDoc.get("country"),
                        (int) accommodationDoc.get("id"),
                        (String) accommodationDoc.get("name"),
                        (String) accommodationDoc.get("neighborhood"));
                resDTOList.add(resDTO);
            }
            PageDTO<ReservationDTO> resPage = new PageDTO<>();
            resPage.setEntries(resDTOList);
            return resPage;
        }catch (Exception e){
            throw new BusinessException(e);
        }finally{documentResDAO.closeConnection();}
    }

    public void modifyUser(ModifiedCustomerDTO oldUserDTO, ModifiedCustomerDTO newUserDTO) throws BusinessException, RemoteException {

        try{
            documentResDAO = new MongoReservationDAO();
            documentRevDAO = new MongoReviewDAO();
            documentUserDAO = new MongoUserDAO();
            if(!oldUserDTO.getEmail().equals(newUserDTO.getEmail()) && documentUserDAO.checkEmail(newUserDTO.getEmail())){
                throw new BusinessException("Email already in use");
            }

            Customer oldUser = new Customer();
            oldUser.setId(oldUserDTO.getId());

            Customer newUser = new Customer();
            newUser.setId(oldUserDTO.getId());
            newUser.setCountry(newUserDTO.getCountry());
            newUser.setFirstName(newUserDTO.getFirstName());
            newUser.setLastName(newUserDTO.getLastName());
            newUser.setAddress(newUserDTO.getAddress());
            newUser.setPhone(newUserDTO.getPhone());
            newUser.setEmail(newUserDTO.getEmail());
            newUser.setPassword(newUserDTO.getPassword());
            newUser.setType("customer");
            documentUserDAO.startTransaction();
            if(!newUserDTO.getFirstName().equals(oldUserDTO.getFirstName()) ||
                !newUserDTO.getLastName().equals(oldUserDTO.getLastName()) ||
                !newUserDTO.getCountry().equals(oldUserDTO.getCountry())){

                LinkedList<Document> resDocs = (LinkedList<Document>) documentResDAO.getCustomerReservations(newUser);
                LinkedList<Document> revDocs = (LinkedList<Document>) documentRevDAO.getReviewsForCustomer(newUser);
                Reservation oldRes = new Reservation();
                Reservation newRes = new Reservation();
                Review oldRev = new Review();
                Review newRev = new Review();
                Accommodation acc = new Accommodation();
                if(!resDocs.isEmpty()){
                    for(Document doc : resDocs){

                        Document accDoc =(Document) doc.get("accommodation");
                        oldRes.setId(doc.getInteger("_id"));
                        newRes.setId(oldRes.getId());
                        acc.setId((accDoc.getInteger("id")));
                        acc.setNeighborhood(accDoc.getString("neighborhood"));
                        acc.setName(accDoc.getString("name"));
                        newRes.setAccommodation(acc);
                        newRes.setCustomer(newUser);

                        Date startDate = (Date) doc.get("start_date");
                        LocalDate startDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        Date endDate =  (Date) doc.get("end_date");
                        LocalDate endDateCasted= startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        newRes.setStartDate(startDateCasted);
                        newRes.setEndDate(endDateCasted);


                        newRes.setTotalCost(doc.getDouble("cost"));
                        newRes.setCustomer(newUser);
                        documentResDAO.modifyReservation(oldRes, newRes);
                    }
                }
                if(!revDocs.isEmpty()){
                    for(Document doc : revDocs){
                        Document accDoc =(Document) doc.get("accommodation");
                        oldRev.setId(doc.getInteger("_id"));
                        newRev.setId(oldRev.getId());
                        acc.setId((accDoc.getInteger("id")));
                        acc.setName(accDoc.getString("name"));
                        newRev.setAccommodation(acc);
                        newRev.setCustomer(newUser);
                        newRev.setComment((String) doc.get("comment"));

                        Date date = (Date) doc.get("date");
                        LocalDate dateCasted= date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        newRev.setDate(dateCasted);

                        newRev.setRate(doc.getInteger("rate"));
                        documentRevDAO.modifyReview(oldRev, newRev);
                    }
                }
            }
            documentUserDAO.modifyAccountInfo(oldUser,newUser);
            graphCustomerDAO.modifyAccountInfo(oldUser,newUser);
            documentResDAO.commitTransaction();
            documentRevDAO.commitTransaction();
            documentUserDAO.commitTransaction();
        }catch(Exception e){
            documentUserDAO.abortTransaction();
            throw new BusinessException(e);
        }finally{
            documentUserDAO.closeConnection();
        }
    }

    public PageDTO<AccommodationDTO> showSuggestedAccommodations(CustomerDTO customerDTO) throws BusinessException, RemoteException{
        try{
            Customer customer = new Customer();
            customer.setId(customerDTO.getId());
            LinkedList<Record> accommodations=(LinkedList<Record>) graphAccDAO.showSuggestedAccommodation(customer);
            List<AccommodationDTO> accDTOList = new LinkedList<>();
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
    public PageDTO<AccommodationDTO> showAccommodationOfSuggestedRenter(CustomerDTO customerDTO) throws BusinessException, RemoteException{
        try{
            Customer customer = new Customer();
            customer.setId(customerDTO.getId());
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

    public PageDTO<CustomerReviewDTO> getOwnReviews(CustomerDTO customerDTO) throws BusinessException,RemoteException{
        try {
            Customer customer = new Customer();
            customer.setId(customerDTO.getId());
            LinkedList<CustomerReviewDTO> customerReviewDTOList = new LinkedList<CustomerReviewDTO>();
            documentRevDAO = new MongoReviewDAO();
            List<Document> docs = documentRevDAO.getReviewsForCustomer(customer);
            if(!docs.isEmpty()) {
                for (Document doc : docs) {

                    Document accommodationDoc = (Document) doc.get("accommodation");

                    CustomerReviewDTO customerReviewDTO = new CustomerReviewDTO(
                            (int) doc.get("_id"),
                            (int) accommodationDoc.get("id"),
                            (String) accommodationDoc.get("name"),
                            (int) doc.get("rate"),
                            (String) doc.get("comment")
                    );

                    customerReviewDTOList.add(customerReviewDTO);
                }
            }
        PageDTO<CustomerReviewDTO> pageDTO = new PageDTO<CustomerReviewDTO>();
        pageDTO.setEntries(customerReviewDTOList);
        return pageDTO;
        }catch(Exception e){
            throw new BusinessException(e);
        }

    }

    public void deleteReservation(ReservationDTO reservationDTO) throws BusinessException, RemoteException {
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();
            Reservation reservation =new Reservation();
            reservation.setId(reservationDTO.getId());
            Accommodation acc =new Accommodation();
            acc.setId(reservationDTO.getId());
            reservation.setAccommodation(acc);
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
