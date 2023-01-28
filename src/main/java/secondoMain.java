import it.unipi.lsmsd.nysleep.DTO.ModifiedCustomerDTO;
import it.unipi.lsmsd.nysleep.business.CustomerServices;

public class secondoMain {
    public static void main(String[] args){
        CustomerServices cs = new CustomerServices();
        try
        {
            cs.modifyUser(oldCustDTO,newCustDTO);
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

}
