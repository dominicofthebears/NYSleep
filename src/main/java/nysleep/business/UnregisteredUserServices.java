package nysleep.business;

import com.mongodb.internal.connection.Connection;
import com.mongodb.session.ClientSession;
import nysleep.DAO.mongoDB.MongoReviewDAO;
import nysleep.DAO.neo4jDB.NeoCustomerDAO;
import nysleep.DAO.neo4jDB.NeoRenterDAO;
import nysleep.business.exception.BusinessException;
import nysleep.model.Customer;
import nysleep.model.RegisteredUser;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.model.Renter;

public class UnregisteredUserServices extends UserServices{
    private MongoUserDAO usrDAO;
    private NeoCustomerDAO custDAO;
    private NeoRenterDAO rentDAO;

    public RegisteredUser register(String firstName, String lastName, String email,
                            String password, String url_prof_pic, String type,String address ,String country,
                            String phone, String workEmail) throws BusinessException{

        if(usrDAO.authenticate(email, password) == null){
            int id = usrDAO.getLastId();
            if (type.equals("customer")){
                Customer to_insert = new Customer(id, firstName, lastName, email, password, url_prof_pic, type, address, country, phone);
                try {
                    usrDAO.register(to_insert);
                } catch (Exception e){
                    throw new BusinessException(e);
                }

                try {
                    custDAO.register(to_insert);
                } catch (Exception e){
                    throw new BusinessException(e);
                }


                return to_insert;
            }
            else if (type.equals("renter")) {
                Renter to_insert = new Renter(id, firstName, lastName, email, password, url_prof_pic, type, workEmail, phone);
                usrDAO.register(to_insert);
                return to_insert;
            }
        }
        else{
            throw new BusinessException("Email already used");
        }
        return null;
    }
}
