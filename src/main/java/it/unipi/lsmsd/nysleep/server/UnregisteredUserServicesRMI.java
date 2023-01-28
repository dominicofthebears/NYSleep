package it.unipi.lsmsd.nysleep.server;

import it.unipi.lsmsd.nysleep.DTO.AccommodationDTO;
import it.unipi.lsmsd.nysleep.DTO.PageDTO;
import it.unipi.lsmsd.nysleep.DTO.RegisteredUserDTO;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface UnregisteredUserServicesRMI extends Remote {
    public RegisteredUserDTO register(String firstName, String lastName, String email,
                                      String password, String url_prof_pic, String type, String address, String country,
                                      String phone, String workEmail) throws BusinessException, RemoteException;

    public RegisteredUserDTO login(String email, String password) throws BusinessException, RemoteException;

    public PageDTO<AccommodationDTO> showSearchAcc  (LocalDate startDate, LocalDate endDate, int numPeople, String neighborhood, double price) throws BusinessException, RemoteException;


}
