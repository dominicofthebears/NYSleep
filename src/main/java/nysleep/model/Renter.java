package nysleep.model;

public class Renter extends RegisteredUser {
    private String workEmail;
    private String phone;

    public Renter(){

    }

    public Renter(String workEmail, String phone) {
        this.workEmail = workEmail;
        this.phone = phone;
    }


    public String getWorkEmail() {return workEmail;}

    public void setWorkEmail(String workEmail) {this.workEmail = workEmail;}

    public String getPhone() {return phone;}

    public void setPhoneNumber(String phone) {this.phone = phone;}
}
