package nysleep.model;

import java.time.LocalDate;

public class Reservation {
    private LocalDate dateRes;
    private LocalDate dataStart;
    private LocalDate dataEnd;
    private float totalCost;
    private int numPeople;
    private Customer customer;
    private Accommodation accommodation;

    public Reservation(){

    }

    public Reservation(LocalDate dateRes, LocalDate dataStart, LocalDate dataEnd, float totalCost, int numPeople, Customer customer, Accommodation accommodation) {
        this.dateRes = dateRes;
        this.dataStart = dataStart;
        this.dataEnd = dataEnd;
        this.totalCost = totalCost;
        this.numPeople = numPeople;
        this.customer = customer;
        this.accommodation = accommodation;
    }

    public LocalDate getDateRes() {return dateRes;}

    public void setDateRes(LocalDate dateRes) {this.dateRes = dateRes;}

    public LocalDate getDataStart() {return dataStart;}

    public void setDataStart(LocalDate dataStart) {this.dataStart = dataStart;}

    public LocalDate getDataEnd() {return dataEnd;}

    public void setDataEnd(LocalDate dataEnd) {this.dataEnd = dataEnd;}

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
