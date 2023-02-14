package it.unipi.lsmsd.nysleep.DTO;

import java.io.Serializable;

public class CustomerDTO  extends RegisteredUserDTO implements Serializable {
    private String country;


    public CustomerDTO(int id, String firstName, String lastName, String country, String email, String password){
        super(id, firstName, lastName, email, password, "customer");
        this.country=country;
    }

    public CustomerDTO() {

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
