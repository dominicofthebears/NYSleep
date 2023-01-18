package nysleep.DTO;

public class RenterDTO extends RegisteredUserDTO{
    private String workEmail;
    private String phone;


    public RenterDTO(int id, String firstName, String lastName, String workEmail, String phone, String email, String password){
        super(id, firstName, lastName, email, password);
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
