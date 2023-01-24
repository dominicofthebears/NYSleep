package nysleep.business;

import nysleep.DAO.mongoDB.*;
import nysleep.DAO.neo4jDB.*;
import nysleep.DTO.*;
import nysleep.business.exception.BusinessException;
import nysleep.model.Accommodation;
import nysleep.model.Renter;


import nysleep.model.Reservation;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class UserServices {

    protected MongoAccommodationDAO documentAccDAO;
    protected MongoReviewDAO documentRevDAO;
    protected MongoUserDAO documentUserDAO;
    protected MongoReservationDAO documentResDAO;

    protected NeoCustomerDAO graphCustomerDAO = new NeoCustomerDAO();
    protected NeoRenterDAO graphRenterDAO = new NeoRenterDAO();
    protected NeoAccommodationDAO graphAccDAO = new NeoAccommodationDAO();
    protected NeoReviewDAO graphRevDAO  = new NeoReviewDAO();

    public UserServices(){}

    public PageDTO<AccommodationDTO> showHomePage(int numPage) throws BusinessException {
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

    public PageDTO<AccReviewDTO> showAccommodationReviews(Accommodation acc) throws BusinessException {
        PageDTO<AccReviewDTO> pageDTO;
        try {
            documentRevDAO = new MongoReviewDAO();
            List<Document> docs = documentRevDAO.getReviewsForAcc(acc);
            LinkedList<AccReviewDTO> AccReviewDTOList = new LinkedList<>();
            for (Document doc : docs) {

                Document customerDoc = (Document) doc.get("customer");

                AccReviewDTO accReviewDTO = new AccReviewDTO(
                        (int) customerDoc.get("id")
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

    public PageDTO<AccommodationDTO> showRenterAccommodations(Renter renter, int numPage) throws BusinessException {
        PageDTO<AccommodationDTO> AccPage;
        try {

            documentAccDAO = new MongoAccommodationDAO();
            List<Document> docs = documentAccDAO.getSearchedAcc(renter);

            LinkedList<AccommodationDTO> accDTOList = new LinkedList<>();
            for (Document doc : docs) {                                                    //iterate all over the documents and extract accommodations to put in the DTO
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



    public PageDTO<AccommodationDTO> showSearchAcc  (LocalDate startDate, LocalDate endDate, int numPeople, String neighborhood,double price) throws BusinessException{
      try{
        documentAccDAO = new MongoAccommodationDAO();
        List<Document> results = documentAccDAO.getSearchedAcc(startDate,endDate, numPeople,neighborhood, price);
        LinkedList<AccommodationDTO> accDTOList = new LinkedList<>();
        for (Document doc : results) {
            LinkedList<String> picsURL = (LinkedList<String>) doc.get("images_URL");
            AccommodationDTO accDTO = new AccommodationDTO(
                    (int) doc.get("_id"),
                    (String) doc.get("name"),
                    (String) doc.get("neighborhood"),
                    (double) doc.get("rating"),
                    picsURL.get(0));
            accDTOList.add(accDTO);
        }
        PageDTO<AccommodationDTO> accommodations = new PageDTO<>();
        accommodations.setEntries(accDTOList);
        return accommodations;
      }catch(Exception e){
          throw new BusinessException("Search Failed");
      }finally{
          documentAccDAO.closeConnection();
      }
    }

    public AccommodationDetailsDTO showAccDetails(Accommodation acc) throws BusinessException{
        try{

            documentAccDAO = new MongoAccommodationDAO();
            Document doc = documentAccDAO.getAccommodation(acc);

            Document renterDoc = (Document) doc.get("renter");
            AccommodationDetailsDTO accDetailsDTO = new AccommodationDetailsDTO(
                    (int) doc.get("_id"),
                    (String) doc.get("name"),
                    (String) doc.get("neighborhood"),
                    (double) doc.get("rating"),
                    (List<String>) doc.get("images_URL"),
                    (int) doc.get("num_beds"),
                    (List<String>) doc.get("amenities"),
                    (double) doc.get("price"),
                    (String) renterDoc.get("first_name"),
                    (String) renterDoc.get("last_name"),
                    (String) renterDoc.get("work_email"),
                    (String) renterDoc.get("phone")
            );

            return accDetailsDTO;

        }catch(Exception e){
            throw new BusinessException(e);
        }finally{
            documentAccDAO.closeConnection();
        }
    }

}
