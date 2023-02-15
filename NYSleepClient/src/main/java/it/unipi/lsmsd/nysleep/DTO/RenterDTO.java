package it.unipi.lsmsd.nysleep.DTO;

import java.io.Serializable;

public class RenterDTO extends RegisteredUserDTO implements Serializable {
    private String workEmail;
    private String phone;

    public RenterDTO(){}

    public RenterDTO(int id, String firstName, String lastName, String workEmail,
                     String phone, String email, String password){
        super(id, firstName, lastName, email, password, "renter");
        this.workEmail=workEmail;
        this.phone=phone;

    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
