package it.unipi.lsmsd.nysleep.business.RMI;

import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RenterServicesRMI extends Remote {


    public void modifyUser(ModifiedRenterDTO oldRenterDTO, ModifiedRenterDTO newRenterDTO) throws BusinessException, RemoteException;
    public void addAccommodation(AccommodationDetailsDTO accDTO) throws BusinessException, RemoteException;
    public void removeAccommodation(AccommodationDetailsDTO accDTO) throws BusinessException, RemoteException;
    public void modifyAccommodation(ModifiedAccDTO oldAccDTO, ModifiedAccDTO newAccDTO) throws BusinessException, RemoteException;
    public PageDTO<ReservationDTO> showRenterReservation(RenterDTO renterDTO) throws BusinessException, RemoteException;
    public void deleteReservation(ReservationDTO reservationDTO) throws BusinessException, RemoteException;
    public PageDTO<AccommodationDTO> showRenterAccommodation(RenterDTO renterDTO) throws BusinessException, RemoteException;

}
