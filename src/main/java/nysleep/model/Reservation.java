package nysleep.model;

import java.time.LocalDate;

public class Reservation {
    public int id;
    private LocalDate dateRes;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private Customer customer;
    private Accommodation accommodation;

    public Reservation(){

    }

    public Reservation(int id, LocalDate startDate, LocalDate endDate, double totalCost, Customer customer, Accommodation accommodation) {
        this.id = id;

        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.customer = customer;
        this.accommodation = accommodation;
    }

    public int getId(){return this.id;}
    public void setId(int id){this.id = id;}

    public LocalDate getStartDate() {return startDate;}

    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getEndDate() {return endDate;}

    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

    public double getTotalCost() {return totalCost;}

    public void setTotalCost(double totalCost) {this.totalCost = totalCost;}

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
