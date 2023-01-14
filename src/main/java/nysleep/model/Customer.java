package nysleep.model;

public class Customer extends RegisteredUser{
    private String address;
    private String country;
    private String phone;

    public Customer(){}
    public Customer(int id,String firstName,String lastName,String email,String password,String url_profile_pic,String type,String address, String country, String phone){
        super.setId(id);
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        super.setUrl_prof_pic(url_profile_pic);
        super.setPassword(password);
        super.setType(type);
        this.address = address;
        this.country = country;
        this.phone = phone;
    }

    public Customer(int customerId){
        super.setId(customerId);
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address=address;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country=country;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phoneNum){
        this.phone=phone;
    }

    @Override
    public String toString(){
        return "Customer{"+
                "address='"+ address + '\''+
                ", country='"+ country + '\''+
                ", phone='"+ phone +
                '}';
    }
}
