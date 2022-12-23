package nysleep.DAO;
import nysleep.model.Accommodation;
import nysleep.DTO.PageDTO;
import nysleep.DTO.AccommodationDTO;


public interface AccommodationDAO {
     void createAccommodation(Accommodation acc);
     void deleteAccommodation(Accommodation acc);
     void updateRating(Accommodation acc, int rating);
     void updateAccommodation(Accommodation oldACc, Accommodation newAcc)
     PageDTO<AccomodationDTO> viewAccommodationHomePage();
     AccommodationDTO viewAccommodation(Accommodation acc);



}
