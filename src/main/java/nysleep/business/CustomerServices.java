package nysleep.business;

import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.DTO.AccommodationDetailsDTO;
import nysleep.DTO.RenterDetailsDTO;
import nysleep.model.Renter;

public class CustomerServices extends UserServices{
    private MongoUserDAO userDAO;

    public CustomerServices(){
        this.userDAO = new MongoUserDAO();
    }

    //Get the renterDTO's of an accommodation
    public RenterDetailsDTO getAccRenter(Renter rent){
        Renter renter = (Renter) userDAO.getUser(rent);
        RenterDetailsDTO renterDTO = new RenterDetailsDTO(renter.getFirstName()
                ,renter.getLastName()
                ,renter.getWorkEmail()
                ,renter.getPhone()
                , renter.getUrl_prof_pic());
        return renterDTO;
    }
}
