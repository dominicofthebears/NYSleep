import com.mongodb.client.ClientSession;
import jdk.vm.ci.code.Register;
import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoReservationDAO;
import nysleep.DAO.mongoDB.MongoReviewDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.DAO.neo4jDB.NeoAccommodationDAO;
import nysleep.DAO.neo4jDB.NeoCustomerDAO;
import nysleep.DAO.neo4jDB.NeoRenterDAO;
import nysleep.DTO.RegisteredUserDTO;
import nysleep.business.UnregisteredUserServices;
import nysleep.model.*;

import javax.lang.model.type.ArrayType;
import java.sql.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App
{
    public static void main( String[] args ){
        MongoUserDAO userDAO = new MongoUserDAO();
        try{
            /*
            userDAO.getSession().startTransaction();
            userDAO.register(customer);
            if(customer.getId() == 20000) {
                System.out.print("Dentro if");
                userDAO.getSession().abortTransaction();*/
            String username = "hajar.vanderperk@example.com";
            String password = "break";
            UnregisteredUserServices us = new UnregisteredUserServices();
            RegisteredUserDTO reg= us.login(username, password);
            System.out.println(reg.getFirstName());
            }
       catch (Exception e){
            e.printStackTrace();
        }
        finally{
            userDAO.getConnection().close();
        }
}



    private static LocalDate date1 = LocalDate.of(2003,4,12);
    private static LocalDate date2 = LocalDate.of(2004,5,13);
    private static LocalDate date3 = LocalDate.of(2006,7,23);
    private static LocalDate date4 = LocalDate.of(2008,2,11);

    public static Customer customer = new Customer(20000
            ,"Kiona"
            ,"Van Weelden"
            , "kiona.vanweelden@example.com"
            , "suzuki"
            ,"https://randomuser.me/api/portraits/thumb/women/47.jpg"
            ,"customer"
            ,"4577 Bornerveldstraat"
            ,"Netherlands"
            ,"(083) 1847928");

    public static ArrayList<String> urls = new ArrayList<String>();

    public static ArrayList<String>amenities = new ArrayList<String>();
    public static ArrayList<Reservation>reservations = new ArrayList<Reservation>();

   /* private static Reservation reservation = new Reservation(
            24249249,
            data2,
            data3,
            344,
            customer,
            accommodation);
*/














}
