import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReservationDAO;
import it.unipi.lsmsd.nysleep.DAO.neo4jDB.NeoReviewDAO;
import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.CustomerServices;
import it.unipi.lsmsd.nysleep.business.RenterServices;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.*;

import java.io.*;
import java.util.Scanner;
import java.time.LocalDate;

public class App
{
    public static void main( String[] args ) throws BusinessException, FileNotFoundException {

        /*MongoReservationDAO resDAO = new MongoReservationDAO();
        System.out.println(resDAO.mostReservedAccommodationForSeason()  );*/




        RenterServices rs = new RenterServices();
        CustomerServices cs = new CustomerServices();
        RenterDetailsDTO renterDetailsDTO = new RenterDetailsDTO(
                10258,
                "Raymond",
                "Forest",
                "Raymond.Forest@NYSleep.com",
                "031-040-5894"
        );
        AccommodationDetailsDTO accommodationDetailsDTO = new AccommodationDetailsDTO(
                16481,
                "pisa_merda",
                "Bronx",
                0,
                null,
                3,
                null,
                430,
                renterDetailsDTO,
                "Entire_townhouse",
                3
        );
        ReservationDTO reservationDTO = new ReservationDTO(
                1001010101,
                LocalDate.of(2023, 01, 28),
                LocalDate.of(2023, 01, 31),
                900,
                0,
                "Kiona",
                "Van Weelden",
                "Netherlands",
                16481,
                "pisa_merda",
                "Bronx"
        );

        AccReviewDTO accReviewDTO = new AccReviewDTO(
                1010912,
                0,
                "Kiona",
                "Van Weelden",
                "Netherlands",
                1,
                "la torre crollera"
        );

        CustomerReviewDTO customerReviewDTO=new CustomerReviewDTO(
                1010912,
                16481,
                "pisa_merda",
                1,
                "la torre crollera"
        );

        try {
            System.out.println("app");
            cs.insertReview(accReviewDTO, customerReviewDTO);
            //rs.removeAccommodation(accommodationDetailsDTO);
        }catch (Exception e){
            e.printStackTrace();
        }


        /*MongoReservationDAO docReservationDAO = new MongoReservationDAO();
        System.out.println(docReservationDAO.neighborhoodRentedByMostNumberOfCountries());*/
        /*Reservation newRes = new Reservation();
        Reservation oldRes = new Reservation();
        oldRes.setId(23);
        newRes.setId(23);
        Customer cust = new Customer();
        cust.setId(2897);
        cust.setFirstName("Jessica");
        cust.setLastName("Bradley");
        cust.setCountry("United States");
        newRes.setCustomer(cust);
        docReservationDAO.modifyReservation(oldRes, newRes);*/


    }
        /*
        CustomerServices cs = new CustomerServices();
        try {
            PageDTO<AccommodationDTO> pageDTO =cs.showSearchAcc(date1, date2, 4, " ", 0);
            System.out.println(pageDTO.toString());
        }catch(Exception e){
            e.printStackTrace();
        }


        /*MongoReservationDAO resDao = new MongoReservationDAO();
        List<Document> docs = resDao.accommodationRentedByMostNumberOfCountries();
        for(Document doc:docs){
            System.out.println(doc);
        }*/

        /*MongoReservationDAO resDAO = new MongoReservationDAO();
        resDAO.deleteReservation(reservation);*/

        /* accDAO = new MongoAccommodationDAO();
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

    private static LocalDate date1 = LocalDate.of(2015,8,18);
    private static LocalDate date2 = LocalDate.of(2023,9,13);
    private static  Accommodation acc = new Accommodation(0, null, null, null, 0,
            0, 0, null, 0, null, 1, null, null);
}
