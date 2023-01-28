package it.unipi.lsmsd.nysleep.DTO;

public class RenterDetailsDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String workEmail;
    private String phone;


    public RenterDetailsDTO(int id, String firstName,String lastName,String workEmail,String phone){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.workEmail = workEmail;
        this.phone = phone;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}