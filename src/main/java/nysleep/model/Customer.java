package nysleep.model;

public class Customer extends RegisteredUser{
    private String address;
    private String country;
    private String phoneNum;

    public Customer(){

    }

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

    public String getPhoneNum(){
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum){
        this.phoneNum=phoneNum;
    }

    @Override
    public String toString(){
        return "Customer{"+
                "address='"+ address + '\''+
                ", country='"+ country + '\''+
                ", phoneNum='"+ phoneNum +
                '}';
    }
}
