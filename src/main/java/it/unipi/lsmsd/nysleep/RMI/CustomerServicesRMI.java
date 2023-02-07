package it.unipi.lsmsd.nysleep.RMI;

import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CustomerServicesRMI extends Remote, UserServicesRMI {
    public void insertReview(AccReviewDTO accReviewDTO, CustomerReviewDTO customerReviewDTO) throws BusinessException, RemoteException;
    public void deleteReview(CustomerReviewDTO customerReviewDTO, AccReviewDTO accReviewDTO) throws BusinessException, RemoteException;
    public void insertReservation(ReservationDTO reservationDTO) throws BusinessException, RemoteException;
    public PageDTO<ReservationDTO> viewReservations(CustomerDTO customerDTO) throws BusinessException, RemoteException;
    public void modifyUser(ModifiedCustomerDTO oldUserDTO, ModifiedCustomerDTO newUserDTO) throws BusinessException, RemoteException;
    public PageDTO<AccommodationDTO> showSuggestedAccommodations(CustomerDTO customerDTO) throws BusinessException, RemoteException;
    public PageDTO<AccommodationDTO> showAccommodationOfSuggestedRenter(CustomerDTO customerDTO) throws BusinessException, RemoteException;
    public PageDTO<CustomerReviewDTO> getOwnReviews(CustomerDTO customerDTO) throws BusinessException,RemoteException;
    public void deleteReservation(ReservationDTO reservationDTO) throws BusinessException, RemoteException;
}
