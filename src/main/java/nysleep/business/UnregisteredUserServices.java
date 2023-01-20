package nysleep.business;

import nysleep.DAO.neo4jDB.NeoCustomerDAO;
import nysleep.DAO.neo4jDB.NeoRenterDAO;
import nysleep.DTO.AdminDTO;
import nysleep.DTO.CustomerDTO;
import nysleep.DTO.RegisteredUserDTO;
import nysleep.DTO.RenterDTO;
import nysleep.business.exception.BusinessException;
import nysleep.model.Admin;
import nysleep.model.Customer;
import nysleep.model.RegisteredUser;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.model.Renter;

public class UnregisteredUserServices extends UserServices {

    //fixare con le transazioni
    public RegisteredUser register(String firstName, String lastName, String email,
                                   String password, String url_prof_pic, String type, String address, String country,
                                   String phone, String workEmail) throws BusinessException {

            documentUserDAO = new MongoUserDAO();
            graphCustomerDAO = new NeoCustomerDAO();
        if (!documentUserDAO.checkEmail(email)) {
            RegisteredUser to_insert = null;
            int id = documentUserDAO.getLastId(documentUserDAO.getCollection());
            if (type.equals("customer")) {
                to_insert = new Customer(id, firstName, lastName, email, password, url_prof_pic,
                        type, address, country, phone);
            } else if (type.equals("renter")) {
                to_insert = new Renter(id, firstName, lastName, email, password, url_prof_pic, type, workEmail, phone);
            }

            try {
                documentUserDAO.startTransaction();
                documentUserDAO.register(to_insert);
                documentUserDAO.commitTransaction();
            } catch (Exception e) {
                documentUserDAO.closeConnection();
                throw new BusinessException(e);
            }

            try {
                graphCustomerDAO.register(to_insert);
            } catch (Exception e) {
                documentUserDAO.abortTransaction();
                throw new BusinessException(e);
            }
            finally {
                documentUserDAO.closeConnection();
            }
            return to_insert;
        } else {
            throw new BusinessException("Email already used");
        }
    }


    public RegisteredUserDTO login(String email, String password) throws BusinessException {
        documentUserDAO = new MongoUserDAO();
        RegisteredUserDTO reg_user;
        RegisteredUser logged_in = documentUserDAO.authenticate(email, password);
        if (logged_in == null) {
            documentUserDAO.closeConnection();
            throw new BusinessException("Username or password are incorrect");
        }
        else {
            if (logged_in instanceof Customer) {
                reg_user = new CustomerDTO(logged_in.getId(), logged_in.getFirstName(), logged_in.getLastName(),
                        ((Customer) logged_in).getCountry(), logged_in.getEmail(), logged_in.getPassword());
            } else if (logged_in instanceof Renter) {
                reg_user = new RenterDTO(logged_in.getId(), logged_in.getFirstName(), logged_in.getLastName(),
                        ((Renter) logged_in).getWorkEmail(), ((Renter) logged_in).getPhone(), logged_in.getEmail(), logged_in.getPassword());
            }
            else{
                reg_user = new AdminDTO(logged_in.getId(), logged_in.getFirstName(), logged_in.getLastName(),
                        ((Admin)logged_in).getTitle(),logged_in.getEmail(), logged_in.getPassword());
            }
            documentUserDAO.closeConnection();
            return reg_user;
        }
    }
}
