package it.unipi.lsmsd.nysleep.business;

import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoUserDAO;
import it.unipi.lsmsd.nysleep.business.RMI.UnregisteredUserServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.Admin;
import it.unipi.lsmsd.nysleep.model.Customer;
import it.unipi.lsmsd.nysleep.model.RegisteredUser;
import it.unipi.lsmsd.nysleep.model.Renter;

import java.rmi.RemoteException;

public class UnregisteredUserServices extends UserServices implements UnregisteredUserServicesRMI {

    public UnregisteredUserServices(){
    }

    public void UnregisteredUserServices(){};
    //fixare con le transazioni
    public RegisteredUserDTO register(String firstName, String lastName, String email,
                                      String password, String url_prof_pic, String type, String address, String country,
                                      String phone, String workEmail) throws BusinessException, RemoteException {

            documentUserDAO = new MongoUserDAO();
        if (!documentUserDAO.checkEmail(email)) {
            RegisteredUser to_insert = null;
            RegisteredUserDTO to_return = null;
            int id = documentUserDAO.getLastId(documentUserDAO.getCollection());
            if (type.equals("customer")) {
                to_insert = new Customer(id, firstName, lastName, email, password, url_prof_pic,
                        type, address, country, phone);
                to_return = new CustomerDTO(id, firstName, lastName, country, email, password);
            } else if (type.equals("renter")) {
                to_insert = new Renter(id, firstName, lastName, email, password, url_prof_pic, type, workEmail, phone);
                to_return = new RenterDTO(id,  firstName,  lastName,  workEmail,  phone,  email, password);
            }

            try {
                documentUserDAO.startTransaction();
                documentUserDAO.register(to_insert);
                graphCustomerDAO.register(to_insert);
                documentUserDAO.commitTransaction();
            } catch (Exception e) {
                documentUserDAO.abortTransaction();
                throw new BusinessException(e);
            }finally {
                documentUserDAO.closeConnection();
            }
            return to_return;
        } else {
            throw new BusinessException("Email already used");
        }
    }


    public RegisteredUserDTO login(String email, String password) throws BusinessException, RemoteException {
        try {
            documentUserDAO = new MongoUserDAO();
            RegisteredUserDTO reg_user;
            RegisteredUser logged_in = documentUserDAO.authenticate(email, password);
            if (logged_in == null) {
                documentUserDAO.closeConnection();
                throw new BusinessException("Username or password are incorrect");
            } else {
                if (logged_in instanceof Customer) {
                    reg_user = new CustomerDTO(logged_in.getId(), logged_in.getFirstName(), logged_in.getLastName(),
                            ((Customer) logged_in).getCountry(), logged_in.getEmail(), logged_in.getPassword());
                } else if (logged_in instanceof Renter) {
                    reg_user = new RenterDTO(logged_in.getId(), logged_in.getFirstName(), logged_in.getLastName(),
                            ((Renter) logged_in).getWorkEmail(), ((Renter) logged_in).getPhone(), logged_in.getEmail(), logged_in.getPassword());
                } else {
                    reg_user = new AdminDTO(logged_in.getId(), logged_in.getFirstName(), logged_in.getLastName(),
                            ((Admin) logged_in).getTitle(), logged_in.getEmail(), logged_in.getPassword());
                }
                documentUserDAO.closeConnection();
                return reg_user;
            }
        }catch(Exception e){
            throw new BusinessException("Login failed");
        }
        finally{
            documentUserDAO.closeConnection();
        }
    }

}
