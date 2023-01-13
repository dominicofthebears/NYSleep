package nysleep.DAO;


import nysleep.model.Accommodation;
import nysleep.model.Renter;

import nysleep.DTO.PageDTO;
import nysleep.DTO.AccommodationDTO;

import java.time.LocalDate;


import nysleep.model.Accommodation;
import nysleep.DTO.PageDTO;
import nysleep.DTO.AccommodationDTO;



public interface AccommodationDAO {
     void createAccommodation(Accommodation acc);
     void deleteAccommodation(int id);
     void updateRating(Accommodation acc, double rating);
     void updateAccommodation(Accommodation oldACc, Accommodation newAcc);








}
