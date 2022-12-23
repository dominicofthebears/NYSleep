package nysleep.model;

public class Renter extends RegisteredUser {
    private int numAccomodations;
    private String workEmail;
    private String phone;

    public Renter(){

    }

    public Renter(int numAccomodations, String workEmail, String phone) {
        this.numAccomodations = numAccomodations;
        this.workEmail = workEmail;
        this.phone = phone;
    }


    public int getNumAccomodations() {return numAccomodations;}

    public void setNumAccomodations(int numAccomodations) {this.numAccomodations = numAccomodations;}

    public String getWorkEmail() {return workEmail;}

    public void setWorkEmail(String workEmail) {this.workEmail = workEmail;}

    public String getPhone() {return phone;}

    public void setPhoneNumber(String phone) {this.phone = phone;}
}
