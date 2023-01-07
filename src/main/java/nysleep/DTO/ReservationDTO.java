package nysleep.DTO;

import java.time.LocalDate;

public class ReservationDTO {
    private LocalDate dateRes;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private double totalCost;
    private int customerId;
    private String customerFirstName;
    private String customerLastName;
    private int accommodationId;
    private String accommodationName;

    public ReservationDTO(LocalDate dateRes, LocalDate dateStart, LocalDate dateEnd, double totalCost, int customerId, String customerFirstName, String customerLastName, int accommodationId, String accommodationName) {
        this.dateRes = dateRes;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.totalCost = totalCost;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
    }

    public LocalDate getDateRes() {return dateRes;}

    public void setDateRes(LocalDate dateRes) {this.dateRes = dateRes;}

    public LocalDate getDateStart() {return dateStart;}

    public void setDateStart(LocalDate dateStart) {this.dateStart = dateStart;}

    public LocalDate getDateEnd() {return dateEnd;}

    public void setDateEnd(LocalDate dateEnd) {this.dateEnd = dateEnd;}

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
              "dateRes='" + dateRes + '\'' +
              ", dateStart='" + dateStart + '\'' +
              ", dateEnd='" + dateEnd + '\'' +
              ", totalCost=" + totalCost +
              ", customerID=" + customerId +
              ", customerFirstName='"+ customerFirstName + '\'' +
              ", customerLastName='"+ customerLastName +'\'' +
              ", accommodationId=" + accommodationId +
              ", accommodationName='"+ accommodationName + '\'' +
              '}';
    }
}
