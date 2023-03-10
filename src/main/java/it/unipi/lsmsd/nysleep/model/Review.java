package it.unipi.lsmsd.nysleep.model;
import java.time.LocalDate;
public class Review {
    private int id;
    private Accommodation accommodation;
    private Customer customer;
    private String comment;
    private int rate;
    private LocalDate date;
    public Review(){}
    public Review(int id, Accommodation acc, Customer cus, String com, int rate, LocalDate date){
        this.id = id;
        this.accommodation=acc;
        this.customer=cus;
        this.comment=com;
        this.rate=rate;
        this.date=date;
    }
    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return "Review{" +
                "ID=" + id +
                ", customerId='"+ customer.getId() + '\'' +
                ", AccId='"+ accommodation.getId() +'\'' +
                ", rate=" + rate +
                ", date='" + date.toString() +'\'' +
                '}';
    }
}
