package nysleep.model;
import java.time.LocalDate;
public class Review {
    private long accommodationId;
    private String accommodationName;
    private long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String countryCustomer;
    private String comment;
    private int rate;
    private LocalDate date;

    public Review(long accommodationId, String accommodationName, long customerId, String customerFirstName, String customerLastName, String countryCustomer, String com, int rate, LocalDate date){
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.countryCustomer = countryCustomer;
        this.comment=com;
        this.rate=rate;
        this.date=date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getCountryCustomer() {
        return countryCustomer;
    }

    public void setCountryCustomer(String countryCustomer) {
        this.countryCustomer = countryCustomer;
    }
}
