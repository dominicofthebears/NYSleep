import com.mongodb.client.ClientSession;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import com.mongodb.internal.operation.AggregateResponseBatchCursor;
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
import org.bson.Document;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.NullValue;

import javax.lang.model.type.ArrayType;
import java.sql.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class App
{
    public static void main( String[] args ){

        NeoAccommodationDAO neoAccommodationDAO = new NeoAccommodationDAO();
        ArrayList<Record> list= (ArrayList<Record>) neoAccommodationDAO.showAccommodationOfLikedRenter(customerD);
        for(Record rec : list){
            double rate;
            if(!rec.get("rating").isNull()){
                rate=rec.get("rating").asDouble();}
            else
                rate=0;
            System.out.println(rec.get("id").asInt() + rec.get("name").asString() + rec.get("neighborhood").asString() + rate);
        }

        /*MongoAccommodationDAO accDAO = new MongoAccommodationDAO();
        accDAO.mostExpensiveAndLeastExpensiveAccommodationForPropertyType();*/


        /*NeoAccommodationDAO accommodationDAO= new NeoAccommodationDAO();

        try{
            ArrayList<Record> list =(ArrayList<Record>) accommodationDAO.showSuggestedAccommodation(customerD);
            for (Record elem:list) {
                System.out.println(elem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
/*
        //MongoUserDAO userDAO = new MongoUserDAO();
        LinkedList list = new LinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        LinkedList list2 = list;
        System.out.println(list2);
*/

        /*
            useAO.getSession().startTransaction();
            userDAO.register(customer);
            if(customer.getId() == 20000) {
                System.out.print("Dentro if");
                userDAO.getSession().abortTransaction();
            String username = "hajar.vanderperk@example.com";
            String password = "break";
            UnregisteredUserServices us = new UnregisteredUserServices();
            RegisteredUserDTO reg= us.login(username, password);
            System.out.println(reg.getFirstName());
            */


        //salvo  }
        //salvo  catch (Exception e){
        //salvo      e.printStackTrace();
        //salvo  }
        //salvo  finally{
            //resDAO.closeConnection();
        //salvo  }






/*
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

    public static Customer customerD = new Customer(64
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

    public static Customer customerD = new Customer(36
            ,"Kiona"
            ,"Van Weelden"
            , "kiona.vanweelden@example.com"
            , "suzuki"
            ,"https://randomuser.me/api/portraits/thumb/women/47.jpg"
            ,"customer"
            ,"4577 Bornerveldstraat"
            ,"Netherlands"
            ,"(083) 1847928");
}
