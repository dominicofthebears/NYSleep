package nysleep.model;

public class Customer extends RegisteredUser{
    private String address;
    private String country;
    private String phone;

    public Customer(){

    }
    public Customer(String address, String country, String phone){}

    public Customer(long customerId){
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
