package nysleep.DAO.neo4jDB;

import nysleep.DAO.ReservationDAO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.ReservationDTO;
import nysleep.model.Customer;
import nysleep.model.Renter;
import nysleep.model.Reservation;

public class NeoReservationDAO implements ReservationDAO {
    @Override
    public void createReservation(Reservation reservation) {

    }

    @Override
    public void deleteReservation(Reservation reservation) {

    }

    public PageDTO<ReservationDTO> getRenterReservations(Renter renter) {
        return null;
    }


}
