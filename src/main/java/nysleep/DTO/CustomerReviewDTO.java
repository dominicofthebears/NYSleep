package nysleep.DTO;

public class CustomerReviewDTO {
    private long accommodationId;
    private String accommodationName;
    private int rate;
    private String comment;


    public CustomerReviewDTO(long accommodationId, String accommodationName, int rate, String comment) {
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.rate = rate;
        this.comment = comment;
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
}
