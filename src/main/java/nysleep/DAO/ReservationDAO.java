package nysleep.DAO;

import nysleep.model.Reservation;
import nysleep.model.Customer;
import nysleep.model.Accommodation;
import nysleep.model.Renter;

import  nysleep.DTO.PageDTO;
import  nysleep.DTO.ReservationDTO;


public interface ReservationDAO {

    void createReservation(Reservation reservation);

    void deleteReservation(Reservation reservation);

    PageDTO<ReservationDTO> getAccReservations(Accommodation acc);

    PageDTO<ReservationDTO> getRenterReservations(Renter renter);

    PageDTO<ReservationDTO> getCustomerReservations(Customer  customer);


}
