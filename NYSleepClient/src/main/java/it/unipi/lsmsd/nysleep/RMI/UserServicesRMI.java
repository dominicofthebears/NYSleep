package it.unipi.lsmsd.nysleep.RMI;

import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface UserServicesRMI extends Remote {

    public PageDTO<AccommodationDTO> showHomePage(int skip, int limit) throws BusinessException, RemoteException;

    public PageDTO<AccReviewDTO> showAccommodationReviews(AccommodationDetailsDTO DTO) throws BusinessException, RemoteException;

    public PageDTO<AccommodationDTO> showRenterAccommodations(RenterDTO renterDTO) throws BusinessException, RemoteException;

    public PageDTO<AccommodationDTO> showSearchAcc
            (LocalDate startDate, LocalDate endDate, int numPeople, String neighborhood, double price, int skip, int limit) throws BusinessException, RemoteException;

    public AccommodationDetailsDTO showAccDetails(AccommodationDTO accDTO) throws BusinessException, RemoteException;

}


