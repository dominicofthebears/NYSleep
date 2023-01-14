package nysleep.business;

import nysleep.DAO.UserDAO;
import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoReviewDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.DTO.*;
import nysleep.model.Accommodation;
import nysleep.model.RegisteredUser;
import nysleep.model.Renter;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class UserServices {
    private MongoAccommodationDAO accDAO;
    private MongoReviewDAO revDAO;
    private MongoUserDAO userDAO;

    public UserServices(){
        this.accDAO = new MongoAccommodationDAO();
        this.revDAO = new MongoReviewDAO();
        this.userDAO = new MongoUserDAO();
    }

    public void modifyUser(RegisteredUser oldUser,RegisteredUser newUser){
        userDAO.modifyAccountInfo(oldUser,newUser);
    }

    public PageDTO<AccommodationDTO> showHomePage(int numPage){
        int limit = 15;
        ArrayList<Document> docs = (ArrayList<Document>) accDAO.getAccHomePage(numPage*limit, limit);
        List<AccommodationDTO> accDTOList = new ArrayList<AccommodationDTO>();

        for(Document doc: docs){                                                    //iterate all over the documents and extract accommodations to put in the DTO
            ArrayList<String> picsURL = (ArrayList<String>) doc.get("images_URL");
            AccommodationDTO accDTO = new AccommodationDTO(
                    (int) doc.get("_id"),
                    (String) doc.get("name"),
                    (String) doc.get("neighborhood"),
                    (double) doc.get("rating"),
                    picsURL.get(0));
            accDTOList.add(accDTO);
        }

        PageDTO<AccommodationDTO> AccHomePage = new PageDTO<AccommodationDTO>();
        AccHomePage.setEntries(accDTOList);

        return AccHomePage;
    }

    public PageDTO<AccReviewDTO> showAccommodationReviews(Accommodation acc){
        return revDAO.getReviewsForAcc(acc);
    }

    public PageDTO<AccommodationDTO> showRenterAccommodations(Renter renter, int numPage){
        int limit = 15;

        List<Document> docs = accDAO.getSearchedAcc(renter, numPage*limit,limit);

        List<AccommodationDTO> accDTOList = new ArrayList<AccommodationDTO>();
        for(Document doc: docs){                                                    //iterate all over the documents and extract accommodations to put in the DTO
            ArrayList<String> picsURL = (ArrayList<String>) doc.get("images_URL");
            AccommodationDTO accDTO = new AccommodationDTO(
                    (int) doc.get("_id"),
                    (String) doc.get("name"),
                    (String) doc.get("neighborhood"),
                    (double) doc.get("rating"),
                    picsURL.get(0));
            accDTOList.add(accDTO);
        }
        PageDTO<AccommodationDTO> AccPage = new PageDTO<AccommodationDTO>();
        AccPage.setEntries(accDTOList);

        return AccPage;
    }

    public PageDTO<AccommodationDTO> showSearchAcc(LocalDate startDate, LocalDate endDate, int numPeople, String neighborhood,double price,int numPage){
        //aggiungi controlli
        int limit=15;
        //Document docs = accDAO.getSearchedAcc(startDate, endDate, numPeople, neighborhood, price,limit*numPage,);
        return null;
    }

    public AccommodationDetailsDTO showAccDetails(Accommodation acc){
        Document doc = accDAO.getAccommodation(acc);
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
    }

}
