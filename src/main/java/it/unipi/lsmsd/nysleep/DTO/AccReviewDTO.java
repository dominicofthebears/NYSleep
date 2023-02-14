package it.unipi.lsmsd.nysleep.DTO;

import java.io.Serializable;
import java.time.LocalDate;

//reviews about an accommodation
public class AccReviewDTO implements Serializable {
    private int id;
    private int customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerCountry;
    private int rate;
    private String comment;
    private LocalDate date;

    public AccReviewDTO(){}
    public AccReviewDTO(int id, int customerId, String customerFirstName, String customerLastName, String customerCountry, int rate, String comment, LocalDate date) {
        this.id = id;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerCountry = customerCountry;
        this.rate = rate;
        this.comment = comment;
        this.date = date;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString(){
        return "Customer: "+ customerFirstName + ' ' + customerLastName +
               "\nFrom: "+ customerCountry +
               "\nrate: " + rate +
               "\ncomment: " + comment +
               "\ndate: " + date ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
