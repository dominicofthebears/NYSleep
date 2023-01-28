package it.unipi.lsmsd.nysleep.model;

public class Renter extends RegisteredUser {
    private String workEmail;
    private String phone;

    public Renter(){}

    public Renter(int id,String firstName,String lastName,String email,String password,String url_profile_pic,String type,String workEmail, String phone) {
        super.setId(id);
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        super.setUrl_prof_pic(url_profile_pic);
        super.setPassword(password);
        super.setType(type);
        this.workEmail = workEmail;
        this.phone = phone;
    }


    public String getWorkEmail() {return workEmail;}

    public void setWorkEmail(String workEmail) {this.workEmail = workEmail;}

    public String getPhone() {return phone;}

    public void setPhoneNumber(String phone) {this.phone = phone;}
}
