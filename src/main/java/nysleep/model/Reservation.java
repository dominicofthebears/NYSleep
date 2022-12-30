package nysleep.model;

import java.time.LocalDate;

public class Reservation {
    private LocalDate dateRes;
    private LocalDate startDate;
    private LocalDate endDate;
    private float totalCost;
    private int numPeople;
    private Customer customer;
    private Accommodation accommodation;

    public Reservation(){

    }

    public Reservation(LocalDate dateRes, LocalDate startDate, LocalDate endDate, float totalCost, int numPeople, Customer customer, Accommodation accommodation) {
        this.dateRes = dateRes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.numPeople = numPeople;
        this.customer = customer;
        this.accommodation = accommodation;
    }

    public LocalDate getDateRes() {return dateRes;}

    public void setDateRes(LocalDate dateRes) {this.dateRes = dateRes;}

    public LocalDate getStartDate() {return startDate;}

    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getEndDate() {return endDate;}

    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

    public float getTotalCost() {return totalCost;}

    public void setTotalCost(float totalCost) {this.totalCost = totalCost;}

    public int getNumPeople() {return numPeople;}

    public void setNumPeople(int numPeople) {this.numPeople = numPeople;}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }
}
