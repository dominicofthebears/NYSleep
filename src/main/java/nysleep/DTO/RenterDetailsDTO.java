package nysleep.DTO;

public class RenterDetailsDTO {
    private String firstName;
    private String lastName;
    private String workEmail;
    private String phone;
    private String urlProfilePic;

    public RenterDetailsDTO(String firstName,String lastName,String workEmail,String phone,String urlProfilePic){
        this.firstName = firstName;
        this.lastName = lastName;
        this.workEmail = workEmail;
        this.phone = phone;
        this.urlProfilePic = urlProfilePic;
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

    public String getUrlProfilePic() {
        return urlProfilePic;
    }

    public void setUrlProfilePic(String urlProfilePic) {
        this.urlProfilePic = urlProfilePic;
    }
}
