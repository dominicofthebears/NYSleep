package nysleep.business;

import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoReservationDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;

import nysleep.DAO.neo4jDB.NeoAccommodationDAO;
import nysleep.DAO.neo4jDB.NeoCustomerDAO;
import nysleep.DAO.neo4jDB.NeoRenterDAO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.ReservationDTO;
import nysleep.business.exception.BusinessException;
import nysleep.model.Accommodation;
import nysleep.model.RegisteredUser;
import nysleep.model.Renter;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RenterServices extends UserServices {

    public RenterServices(){}

    public void modifyUser(Renter oldRenter, RegisteredUser newRenter) throws BusinessException {
        try{
            documentUserDAO = new MongoUserDAO();
            graphRenterDAO = new NeoRenterDAO();
            newRenter.setId(oldRenter.getId());
            documentUserDAO.startTransaction();
            documentUserDAO.modifyAccountInfo(oldRenter,newRenter);
            documentUserDAO.commitTransaction();
        }catch(Exception e){
            documentUserDAO.closeConnection();
            throw new BusinessException(e);
        }
        try{
            graphCustomerDAO.modifyAccountInfo(oldRenter,newRenter);
        }catch (Exception e){
            documentUserDAO.abortTransaction();
            documentUserDAO.closeConnection();
            throw new BusinessException(e);
        }
    }

    public void addAccommodation(Accommodation acc) throws BusinessException {
        try{

            documentAccDAO = new MongoAccommodationDAO();
            graphAccDAO = new NeoAccommodationDAO();
            acc.setId(documentResDAO.getLastId(documentResDAO.getCollection()));
            documentAccDAO.startTransaction();
            documentAccDAO.createAccommodation(acc);
            documentAccDAO.commitTransaction();
        }catch(Exception e){
            throw new BusinessException(e);}

        try{
            graphAccDAO.createAccommodation(acc);
        }catch(Exception e){
            documentAccDAO.abortTransaction();
            throw new BusinessException(e);
        }
        finally{
            documentAccDAO.closeConnection();
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

    public void modifyAccommodation(Accommodation oldAcc,Accommodation newAcc) throws BusinessException {
        try{
            documentAccDAO = new MongoAccommodationDAO();
            graphAccDAO = new NeoAccommodationDAO();
            newAcc.setId(oldAcc.getId());
            documentAccDAO.updateAccommodation(oldAcc, newAcc);
            graphAccDAO.updateAccommodation(oldAcc,newAcc);

        }catch(Exception e){
            throw new BusinessException(e);

        }finally{
            documentAccDAO.closeConnection();
        }
    }

    public PageDTO<ReservationDTO> showRenterReservation(Renter renter) throws BusinessException {
        try{
            documentResDAO = new MongoReservationDAO();
            documentAccDAO = new MongoAccommodationDAO();

            ArrayList<Document> accDocs = (ArrayList<Document>) documentAccDAO.getSearchedAcc(renter);
            List<ReservationDTO> resDTOList = new ArrayList<ReservationDTO>();
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




}

