package it.unipi.lsmsd.nysleep.business;

import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoAccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReservationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReviewDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoUserDAO;
import it.unipi.lsmsd.nysleep.DAO.neo4jDB.NeoAccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.neo4jDB.NeoCustomerDAO;
import it.unipi.lsmsd.nysleep.DAO.neo4jDB.NeoRenterDAO;
import it.unipi.lsmsd.nysleep.DAO.neo4jDB.NeoReviewDAO;
import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.Accommodation;
import it.unipi.lsmsd.nysleep.model.Renter;


import it.unipi.lsmsd.nysleep.server.UserServicesRMI;
import org.bson.Document;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


public class UserServices implements UserServicesRMI {

    protected MongoAccommodationDAO documentAccDAO;
    protected MongoReviewDAO documentRevDAO;
    protected MongoUserDAO documentUserDAO;
    protected MongoReservationDAO documentResDAO;

    protected NeoCustomerDAO graphCustomerDAO = new NeoCustomerDAO();
    protected NeoRenterDAO graphRenterDAO = new NeoRenterDAO();
    protected NeoAccommodationDAO graphAccDAO = new NeoAccommodationDAO();
    protected NeoReviewDAO graphRevDAO  = new NeoReviewDAO();

    public UserServices(){}

    public PageDTO<AccommodationDTO> showHomePage(int numPage) throws BusinessException, RemoteException {
        try{

            documentAccDAO = new MongoAccommodationDAO();
            LinkedList<Document> docs = (LinkedList<Document>) documentAccDAO.getAccHomePage();

            List<AccommodationDTO> accDTOList = new LinkedList<>();
            for(Document doc: docs){ //iterate all over the documents and extract accommodations to put in the DTO
                LinkedList<String> picsURL = (LinkedList<String>) doc.get("images_URL");
                AccommodationDTO accDTO = new AccommodationDTO(
                        (int) doc.get("_id"),
                        (String) doc.get("name"),
                        (String) doc.get("neighborhood"),
                        (double) doc.get("rating"),
                        picsURL.get(0));
                accDTOList.add(accDTO);
            }

            PageDTO<AccommodationDTO> AccHomePage = new PageDTO<>();
            AccHomePage.setEntries(accDTOList);
            return AccHomePage;
        }catch(Exception e){
            throw new BusinessException(e);
        }
        finally{
            documentAccDAO.closeConnection();
        }
    }

    public PageDTO<AccReviewDTO> showAccommodationReviews(AccommodationDetailsDTO DTO) throws BusinessException, RemoteException {
        Accommodation acc = new Accommodation();
        acc.setId(DTO.getId());
        PageDTO<AccReviewDTO> pageDTO;
        try {
            documentRevDAO = new MongoReviewDAO();
            List<Document> docs = documentRevDAO.getReviewsForAcc(acc);
            LinkedList<AccReviewDTO> AccReviewDTOList = new LinkedList<>();
            for (Document doc : docs) {

                Document customerDoc = (Document) doc.get("customer");

                AccReviewDTO accReviewDTO = new AccReviewDTO(
                        (int) doc.get("_id")
                        , (int) customerDoc.get("id")
                        , (String) customerDoc.get("first_name")
                        , (String) customerDoc.get("last_name")
                        , (String) customerDoc.get("country")
                        , (int) doc.get("rate")
                        , (String) doc.get("comment"));

                AccReviewDTOList.add(accReviewDTO);
            }
            pageDTO = new PageDTO<>();
            pageDTO.setEntries(AccReviewDTOList);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            documentRevDAO.closeConnection();
        }
        return pageDTO;
    }

    public PageDTO<AccommodationDTO> showRenterAccommodations(RenterDTO renterDTO, int numPage) throws BusinessException, RemoteException {
        Renter renter = new Renter();
        renter.setId(renterDTO.getId());
        PageDTO<AccommodationDTO> AccPage;
        try {

            documentAccDAO = new MongoAccommodationDAO();
            List<Document> docs = documentAccDAO.getSearchedAcc(renter);

            LinkedList<AccommodationDTO> accDTOList = new LinkedList<>();
            for (Document doc : docs) { //iterate all over the documents and extract accommodations to put in the DTO
                LinkedList<String> picsURL = (LinkedList<String>) doc.get("images_URL");
                AccommodationDTO accDTO = new AccommodationDTO(
                        (int) doc.get("_id"),
                        (String) doc.get("name"),
                        (String) doc.get("neighborhood"),
                        (double) doc.get("rating"),
                        picsURL.get(0));
                accDTOList.add(accDTO);
            }
            AccPage = new PageDTO<>();
            AccPage.setEntries(accDTOList);

        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            documentAccDAO.closeConnection();
        }
        return AccPage;
    }



    public PageDTO<AccommodationDTO> showSearchAcc  (LocalDate startDate, LocalDate endDate, int numPeople, String neighborhood,double price) throws BusinessException, RemoteException {
      try{
        documentAccDAO = new MongoAccommodationDAO();
        LinkedList<Document> results = (LinkedList<Document>) documentAccDAO.getSearchedAcc(startDate,endDate, numPeople,neighborhood, price);
        LinkedList<AccommodationDTO> accDTOList = new LinkedList<>();
        for (Document doc : results) {
            AccommodationDTO accDTO = new AccommodationDTO();
            //LinkedList<String> picsURL = (LinkedList<String>) doc.get("images_URL");
            accDTO.setId((int) doc.get("_id"));
            accDTO.setName((String) doc.get("name"));
            accDTO.setNeighborhood((String) doc.get("neighborhood"));
            accDTOList.add(accDTO);
        }
        PageDTO<AccommodationDTO> accommodations = new PageDTO<>();
        accommodations.setEntries(accDTOList);
        return accommodations;
      }catch(Exception e){
          throw new BusinessException(e);
      }finally{
          documentAccDAO.closeConnection();
      }
    }

    public AccommodationDetailsDTO showAccDetails(AccommodationDTO accDTO) throws BusinessException, RemoteException{
        Accommodation acc = new Accommodation();
        acc.setId(accDTO.getId());
        try{
            documentAccDAO = new MongoAccommodationDAO();
            Document doc = documentAccDAO.getAccommodation(acc);
            Document renterDoc = (Document) doc.get("renter");
            RenterDetailsDTO renterDetailsDTO = new RenterDetailsDTO(
                    (int) renterDoc.get("_id"),
                    (String) renterDoc.get("first_name"),
                    (String) renterDoc.get("last_name"),
                    (String) renterDoc.get("work_email"),
                    (String) renterDoc.get("phone")
                    );

            AccommodationDetailsDTO accDetailsDTO = new AccommodationDetailsDTO(
                    (int) doc.get("_id"),
                    (String) doc.get("name"),
                    (String) doc.get("neighborhood"),
                    (double) doc.get("rating"),
                    (List<String>) doc.get("images_URL"),
                    (int) doc.get("num_beds"),
                    (List<String>) doc.get("amenities"),
                    (double) doc.get("price"),
                    renterDetailsDTO,
                    (String) doc.get("property_type"), (int) doc.get("num_rooms"));

            return accDetailsDTO;

        }catch(Exception e){
            throw new BusinessException(e);
        }finally{
            documentAccDAO.closeConnection();
        }
    }

}
