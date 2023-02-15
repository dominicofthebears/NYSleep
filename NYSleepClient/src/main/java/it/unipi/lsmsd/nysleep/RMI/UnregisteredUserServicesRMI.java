package it.unipi.lsmsd.nysleep.RMI;

import it.unipi.lsmsd.nysleep.DTO.RegisteredUserDTO;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UnregisteredUserServicesRMI extends Remote, UserServicesRMI {
    public RegisteredUserDTO register(String firstName, String lastName, String email,
                                      String password, String url_prof_pic, String type, String address, String country,
                                      String phone, String workEmail) throws BusinessException, RemoteException;

    public RegisteredUserDTO login(String email, String password) throws BusinessException, RemoteException;


}
