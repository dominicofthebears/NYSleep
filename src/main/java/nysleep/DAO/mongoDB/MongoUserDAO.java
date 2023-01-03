package nysleep.DAO.mongoDB;

import nysleep.DAO.UserDAO;
import nysleep.DAO.base.MongoBaseDAO;
import nysleep.model.Admin;
import nysleep.model.RegisteredUser;
import nysleep.model.Renter;
import org.bson.Document;

import java.util.ArrayList;
import nysleep.model.Customer;
    public abstract class MongoUserDAO extends MongoBaseDAO implements UserDAO {
        private final String COLLECTION="Users";
        private static Document toDoc(RegisteredUser registeredUser) {
            Document doc = new Document("id",registeredUser.getId())
                    .append("first_name",registeredUser.getFirstName())
                    .append("last_name",registeredUser.getLastName())
                    .append("email",registeredUser.getEmail())
                    .append("password",registeredUser.getPassword())
                    .append("url_prof_pic",registeredUser.getUrl_prof_pic())
                    .append("type",registeredUser.getType());
            if( registeredUser instanceof Customer){
                doc.append("address",((Customer) registeredUser).getAddress())
                        .append("country",((Customer) registeredUser).getCountry())
                        .append("phone",((Customer) registeredUser).getPhone());
            }
            else if(registeredUser instanceof Renter){
                doc.append("work_email",((Renter) registeredUser).getWorkEmail())
                        .append("phone",((Renter) registeredUser).getPhone());
            }
            else if(registeredUser instanceof Admin){
                doc.append("title",((Admin) registeredUser).getTitle());
            }
            return doc;
        }

        @Override
        public void register(RegisteredUser registeredUser) {
            Document doc = toDoc(registeredUser);
            insertDoc(doc, COLLECTION);
        }

        @Override
        public RegisteredUser authenticate(String username, String password) {

            return null;
        }

        @Override
        public void modifyAccountInfo(RegisteredUser user, RegisteredUser modifiedUser) {

        }
    }


