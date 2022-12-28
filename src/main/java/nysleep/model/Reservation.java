package nysleep.model;

import java.time.LocalDate;

public class Reservation {
    private LocalDate dateRes;
    private LocalDate dataStart;
    private LocalDate dataEnd;
    private float totalCost;
    private int numPeople;
    private long customerId;
    private String customerFirstName;
    private String customerLastName;
    private long accommodationId;
    private String accommodationName;

    public Reservation(){

    }

    public Reservation(LocalDate dateRes, LocalDate dataStart, LocalDate dataEnd, float totalCost, int numPeople, long customerId, String customerFirstName, String customerLastName, long accommodationId, String accommodationName) {
        this.dateRes = dateRes;
        this.dataStart = dataStart;
        this.dataEnd = dataEnd;
        this.totalCost = totalCost;
        this.numPeople = numPeople;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
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

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
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

    public long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }
}
