package it.unipi.lsmsd.nysleep.DTO;

public class ModifiedRenterDTO extends RenterDTO {
    private String url_profile_pic;

    public ModifiedRenterDTO(){}
    public ModifiedRenterDTO(int id, String firstName, String lastName, String workEmail, String phone, String email, String password,String url_profile_pic){
        super(id, firstName, lastName, email, password,workEmail,phone);
        this.url_profile_pic = url_profile_pic;
    }

    public String getUrl_profile_pic() {
        return url_profile_pic;
    }

    public void setUrl_profile_pic(String url_profile_pic) {
        this.url_profile_pic = url_profile_pic;
    }
}
