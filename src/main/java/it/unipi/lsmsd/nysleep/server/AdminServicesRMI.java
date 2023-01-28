package it.unipi.lsmsd.nysleep.server;

import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import org.bson.Document;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AdminServicesRMI extends Remote {

    public void modifyUser(AdminDTO oldAdminDTO, AdminDTO newAdminDTO) throws BusinessException, RemoteException;
    public void removeAccommodation(AccommodationDTO accDTO) throws BusinessException, RemoteException;
    public PageDTO<ReservationDTO> showAccReservations(AccommodationDTO accDTO) throws BusinessException,RemoteException;
    public void deleteReview(CustomerReviewDTO customerReviewDTO, AccReviewDTO accReviewDTO) throws BusinessException, RemoteException;
    public void deleteReservation(ReservationDTO reservationDTO) throws BusinessException, RemoteException;

    public Document customerWhoHasSpentTheMost() throws BusinessException, RemoteException;
    public List<Document> mostReservedAccommodationForEachNeighborhood() throws BusinessException, RemoteException;
    public Document customerWithHighestAverageExpense() throws BusinessException, RemoteException;
    public List<Document> mostReservingCountryForNeighborhood() throws BusinessException, RemoteException;
    public List<Document> mostAndLeastExpensiveAccommodationForPropertyType() throws BusinessException, RemoteException;
    public List<Document> averageRatingByCountry() throws BusinessException, RemoteException;
}
