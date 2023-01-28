package it.unipi.lsmsd.nysleep.DAO;

import it.unipi.lsmsd.nysleep.model.Reservation;


public interface ReservationDAO {
    void createReservation(Reservation reservation);

    void deleteReservation(Reservation reservation);



}
