package it.unipi.lsmsd.nysleep.DAO;


import it.unipi.lsmsd.nysleep.model.Accommodation;


public interface AccommodationDAO {
     void createAccommodation(Accommodation acc);
     void deleteAccommodation(Accommodation acc);
     void updateRating(Accommodation acc, double rating);
     void updateAccommodation(Accommodation oldACc, Accommodation newAcc);








}
