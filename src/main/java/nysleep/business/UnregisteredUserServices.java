package nysleep.business;

import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.DTO.*;
import nysleep.business.exception.BusinessException;
import nysleep.model.Admin;
import nysleep.model.Customer;
import nysleep.model.RegisteredUser;
import nysleep.model.Renter;
import org.bson.Document;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class UnregisteredUserServices extends UserServices {

    //fixare con le transazioni
    public RegisteredUser register(String firstName, String lastName, String email,
                                   String password, String url_prof_pic, String type, String address, String country,
                                   String phone, String workEmail) throws BusinessException {

            documentUserDAO = new MongoUserDAO();
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
                graphCustomerDAO.register(to_insert);
                documentUserDAO.commitTransaction();
            } catch (Exception e) {
                documentUserDAO.abortTransaction();
                throw new BusinessException(e);
            }finally {
                documentUserDAO.closeConnection();
            }
            return to_insert;
        } else {
            throw new BusinessException("Email already used");
        }
    }


    public RegisteredUserDTO login(String email, String password) throws BusinessException {
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


    public PageDTO<AccommodationDTO> showSearchAcc  (LocalDate startDate, LocalDate endDate, int numPeople, String neighborhood, double price) throws BusinessException{
        try{
            documentAccDAO = new MongoAccommodationDAO();
            List<Document> results = documentAccDAO.getSearchedAcc(startDate, endDate, numPeople,neighborhood, price);
            LinkedList<AccommodationDTO> accDTOList = new LinkedList<>();
            for (Document doc : results) {
                LinkedList<String> picsURL = (LinkedList<String>) doc.get("images_URL");
                AccommodationDTO accDTO = new AccommodationDTO(
                        (int) doc.get("_id"),
                        (String) doc.get("name"),
                        (String) doc.get("neighborhood"),
                        (double) doc.get("rating"),
                        picsURL.get(0));
                accDTOList.add(accDTO);
            }
            PageDTO<AccommodationDTO> accommodations = new PageDTO<>();
            accommodations.setEntries(accDTOList);
            return accommodations;
        }catch(Exception e){
            throw new BusinessException("Search Failed");
        }finally{
            documentAccDAO.closeConnection();
        }
    }
}
