package it.unipi.lsmsd.nysleep.DTO;

import java.io.Serializable;
import java.time.LocalDate;

public class ReservationDTO implements Serializable {

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private int customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerCountry;
    private int accommodationId;
    private String accommodationName;
    private String accommodationNeighborhood;

    public ReservationDTO(int id, LocalDate startDate, LocalDate endDate, double totalCost, int customerId, String customerFirstName, String customerLastName, String customerCountry, int accommodationId, String accommodationName, String accommodationNeighborhood) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerCountry = customerCountry;
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.accommodationNeighborhood = accommodationNeighborhood;
    }


    public LocalDate getstartDate() {return startDate;}

    public void setstartDate(LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getendDate() {return endDate;}

    public void setendDate(LocalDate endDate) {this.endDate = endDate;}

    public double getTotalCost() {return totalCost;}

    public void setTotalCost(double totalCost) {this.totalCost = totalCost;}

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

    public int getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public String toString(){
        return"From: " + startDate +
              "\nto: " + endDate +
              "\nTotal Cost=" + totalCost +
              "\nCustomer: "+ customerFirstName + ' ' + customerLastName +
              "\nAccommodation: "+ accommodationName ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getAccommodationNeighborhood() {
        return accommodationNeighborhood;
    }

    public void setAccommodationNeighborhood(String accommodationNeighborhood) {
        this.accommodationNeighborhood = accommodationNeighborhood;
    }
}
