package nysleep.model;

public class Renter extends RegisteredUser {
    private int numAccomodations;
    private String workEmail;
    private String phoneNumber;

    public Renter(){

    }

    public Renter(int numAccomodations, String workEmail, String phoneNumber) {
        this.numAccomodations = numAccomodations;
        this.workEmail = workEmail;
        this.phoneNumber = phoneNumber;
    }


    public int getNumAccomodations() {return numAccomodations;}

    public void setNumAccomodations(int numAccomodations) {this.numAccomodations = numAccomodations;}

    public String getWorkEmail() {return workEmail;}

    public void setWorkEmail(String workEmail) {this.workEmail = workEmail;}

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
}
