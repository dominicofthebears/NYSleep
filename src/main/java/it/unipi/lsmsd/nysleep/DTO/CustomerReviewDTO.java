package it.unipi.lsmsd.nysleep.DTO;
//reviews made by a customer
public class CustomerReviewDTO {
    private int id;
    private int accommodationId;
    private String accommodationName;
    private int rate;
    private String comment;


    public CustomerReviewDTO(int id, int accommodationId, String accommodationName, int rate, String comment) {
        this.id=id;
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.rate = rate;
        this.comment = comment;
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
        return "CustomerReviewDTO{" +
                "accommodationId=" + accommodationId +
                ", accommodationName='"+ accommodationName + '\'' +
                ", rate=" + rate +
                ", comment='" + comment +'\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
