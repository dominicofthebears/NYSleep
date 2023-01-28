import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.business.AdminServices;
import it.unipi.lsmsd.nysleep.business.CustomerServices;

public class secondoMain {
    public static void main(String[] args){
        //CustomerServices cs = new CustomerServices();
        AdminServices as = new AdminServices();
        try
        {
            //cs.modifyUser(oldCustDTO,newCustDTO);

           accDTO.setId(5208);
           System.out.println(as.showAccReservations(accDTO).toString());
        }catch(Exception e){e.printStackTrace();}
    }

    private static ModifiedCustomerDTO newCustDTO = new ModifiedCustomerDTO(4,
            "Irma",
            "Thomas",
            "Barletta",
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
            12501,"Ryan","Reynolds","allison.reynolds@example.com","simpson","analyst");

    private static AccommodationDTO accDTO = new AccommodationDTO();

}
