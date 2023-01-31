import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoAccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoReservationDAO;
import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.AdminServices;
import it.unipi.lsmsd.nysleep.business.UserServices;
import it.unipi.lsmsd.nysleep.model.Accommodation;
import org.bson.Document;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class secondoMain {
    public static void main(String[] args) throws RemoteException {
        AdminServices ad =  new AdminServices();
        UserServices user = new UserServices();
        try
        {
            /*
            System.out.println(
                    user.showHomePage(1)
            );

            System.out.println(
                    user.showHomePage(2)

            );*/
            /*RenterDTO renterDTO = new RenterDTO();
            renterDTO.setId(10002);
            AccommodationDTO accDTO = new AccommodationDTO();
            accDTO.setId(1);
            System.out.println(user.showAccDetails(accDTO).getId());*/
            MongoAccommodationDAO mongoAccommodationDAO = new MongoAccommodationDAO();
            //PageDTO<AccommodationDTO> pageDTO=user.showSearchAcc(date1, date2, 3, " ", 200, 0, 10);
            //List<Document> docs = mongoAccommodationDAO.getSearchedAcc(date1, date2, 4, " ", 0);
            /*System.out.println(docs.size());
            for(Document doc:docs) {
                System.out.println(docs);
            }*/
            PageDTO<AccommodationDTO> pageDTO=user.showHomePage(0, 10);
            System.out.println(pageDTO.toString());

        }catch(Exception e){e.printStackTrace();}

    }

    private static List<String> amenities=  new ArrayList<String>();
    private static List<String> imagesURL = new ArrayList<String>();
    private static ModifiedAccDTO oldAccDTO = new ModifiedAccDTO(2,"Lovely Room 1 in BEST AREA; Legal Rental, Spotless",
            imagesURL,
            1,
            1,
            1,
            amenities);
    private static ModifiedAccDTO newAccDTO = new ModifiedAccDTO(2,"pisa merda",
            imagesURL,
            1,
            1,
            1,
            amenities);

    private static ModifiedCustomerDTO newCustDTO = new ModifiedCustomerDTO(4,
            "Irma",
            "Thomas",
            "Livorno",
            "irma.thomas@example.com",
            "napster",
            "(720)-822-0934",
            "https://randomuser.me/api/portraits/thumb/women/71.jpg",
            "6285 Oak Lawn Ave");
    private static ModifiedCustomerDTO oldCustDTO = new ModifiedCustomerDTO(4,
            "Irma",
            "Thomas",
            "United States",
            "irma.thomas@example.com",
            "napster",
            "(720)-822-0934",
            "https://randomuser.me/api/portraits/thumb/women/71.jpg",
            "6285 Oak Lawn Ave");

    private static CustomerDTO custDTO = new CustomerDTO(4,
            "Irma",
            "Thomas",
            "Barletta",
            "irma.thomas@example.com",
            "napster");
    private static AdminDTO oldAdminDTO = new AdminDTO(
            12501,"Allison","Reynolds","allison.reynolds@example.com","simpson","analyst"
    );
    private static AdminDTO newAdminDTO = new AdminDTO(
            12501,"Ryan","Reynolds","allison.reynolds@example.com","simpson","analyst"
    );

    private static ReservationDTO reservationDTO = new ReservationDTO(
            20490,
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

    private static AccReviewDTO accReviewDTO = new AccReviewDTO(
            19966,
            2277,
            "Kiona",
            "Van Weelden",
            "Netherlands",
            1,
            "la torre crollera"
    );

    private static CustomerReviewDTO customerReviewDTO=new CustomerReviewDTO(
            19966,
            15701,
            "pisa_merda",
            1,
            "la torre crollera"
    );

    private static RenterDetailsDTO renterDetailsDTO = new RenterDetailsDTO(
            10258,
            "Raymond",
            "Forest",
            "Raymond.Forest@NYSleep.com",
            "031-040-5894"
    );

    private static AccommodationDetailsDTO accommodationDetailsDTO = new AccommodationDetailsDTO(
            3,
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

    private static LocalDate date1 = LocalDate.of(2023,8,18);
    private static LocalDate date2 = LocalDate.of(2023,9,13);
    private static Accommodation acc = new Accommodation(0, null, null, null, 0,
            0, 0, null, 0, null, 1, null, null);
}
