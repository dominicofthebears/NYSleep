import jdk.vm.ci.code.Register;
import nysleep.DAO.mongoDB.MongoAccommodationDAO;
import nysleep.DAO.mongoDB.MongoReservationDAO;
import nysleep.DAO.mongoDB.MongoUserDAO;
import nysleep.DAO.neo4j.Neo4jReviewsDAO;
import nysleep.model.*;

import javax.lang.model.type.ArrayType;
import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args ){
        //
        //              RIMETTERE MONGOUSERDAO COME ASTRATTA
        //
        ///
        /*
        MongoReservationDAO reservationDAO = new MongoReservationDAO();
        try{
            reservationDAO.deleteReservation(reservation);

       }catch (Exception e){
            e.printStackTrace();
        }*/
        /*Accommodation acc=new Accommodation(1936, "Cozy 4 bedrm house 2nd floor 5min LGA /15min JFK", "East Elmhurst", null, 6, 4.5, 2, "", 4, null, 339, 0, null);
        Neo4jReviewsDAO reviewsDAO = new Neo4jReviewsDAO();
        try{
            double avg=reviewsDAO.getAvgRating(acc);
            System.out.println(avg);
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }












    private static LocalDate data1 = LocalDate.of(2003,4,12);
    private static LocalDate data2 = LocalDate.of(2004,5,13);
    private static LocalDate data3 = LocalDate.of(2006,7,23);
    private static LocalDate data4 = LocalDate.of(2008,2,11);

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


    private static Reservation reservation = new Reservation(
            24249249,
            data1,
            data2,
            data3,
            344,
            customer,
            accommodation);


















}
