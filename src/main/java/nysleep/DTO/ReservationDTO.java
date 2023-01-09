package nysleep.DTO;

import java.time.LocalDate;

public class ReservationDTO {

    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private int customerId;
    private String customerFirstName;
    private String customerLastName;
    private int accommodationId;
    private String accommodationName;

    public ReservationDTO(LocalDate startDate, LocalDate endDate, double totalCost, int customerId, String customerFirstName, String customerLastName, int accommodationId, String accommodationName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
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
        return"ReservationDTO{" +
              ", startDate='" + startDate + '\'' +
              ", endDate='" + endDate + '\'' +
              ", totalCost=" + totalCost +
              ", customerID=" + customerId +
              ", customerFirstName='"+ customerFirstName + '\'' +
              ", customerLastName='"+ customerLastName +'\'' +
              ", accommodationId=" + accommodationId +
              ", accommodationName='"+ accommodationName + '\'' +
              '}';
    }
}
