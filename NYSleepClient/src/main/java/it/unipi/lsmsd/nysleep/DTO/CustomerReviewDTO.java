package it.unipi.lsmsd.nysleep.DTO;

import java.io.Serializable;
import java.time.LocalDate;

//reviews made by a customer
public class CustomerReviewDTO implements Serializable {
    private int id;
    private int accommodationId;
    private String accommodationName;
    private int rate;
    private String comment;
    private LocalDate date;

    public CustomerReviewDTO(){}
    public CustomerReviewDTO(int id, int accommodationId, String accommodationName, int rate, String comment, LocalDate date) {
        this.id=id;
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.rate = rate;
        this.comment = comment;
        this.date = date;
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
        return  "Accommodation: "+ accommodationName +
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
