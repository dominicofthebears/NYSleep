package it.unipi.lsmsd.nysleep.DTO;


public class ModifiedCustomerDTO extends CustomerDTO {

    private String phone;
    private String url_profile_pic;
    private String address;

    public ModifiedCustomerDTO(int id, String firstName, String lastName, String country, String email, String password, String phone, String url_profile_pic, String address){
        super(id, firstName, lastName, country, email,password);
        this.phone = phone;
        this.url_profile_pic=url_profile_pic;
        this.address = address;
    }

    public String getUrl_profile_pic() {
        return url_profile_pic;
    }

    public void setUrl_profile_pic(String url_profile_pic) {
        this.url_profile_pic = url_profile_pic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
