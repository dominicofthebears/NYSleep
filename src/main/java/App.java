import jdk.vm.ci.code.Register;
import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoReservationDAO;
import nysleep.DAO.mongoDB.MongoReviewDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;
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
            System.out.println(reviewDAO.getReviewsForCustomer(customer).getEntries());
       }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static LocalDate date1 = LocalDate.of(2003,4,12);
    private static LocalDate date2 = LocalDate.of(2004,5,13);
    private static LocalDate date3 = LocalDate.of(2006,7,23);
    private static LocalDate date4 = LocalDate.of(2008,2,11);

    public static Customer customer = new Customer(0
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
    public static Accommodation accommodation = new Accommodation(1
            ,"Accommodation1"
            ,"Manahattan"
            ,urls
            ,3
            ,32
            ,34
            ,"Monolocale"
            ,324
            ,amenities
            ,34
            ,3
            ,reservations
    );
        public static Review review = new Review(202030,accommodation,customer,"Molto bellina",4,date1);

   /* private static Reservation reservation = new Reservation(
            24249249,
            data2,
            data3,
            344,
            customer,
            accommodation);
*/

















}
