package nysleep.DTO;

public class ReviewAccDTO {
    private long customId;
    private String customFirstName;
    private String customLastName;
    private String customCountry;
    private int rate;
    private String comment;

    public ReviewAccDTO(long customId, String customFirstName, String customLastName, String customCountry, int rate, String comment) {
        this.customId = customId;
        this.customFirstName = customFirstName;
        this.customLastName = customLastName;
        this.customCountry = customCountry;
        this.rate = rate;
        this.comment = comment;
    }

    public long getCustomId() {
        return customId;
    }

    public void setCustomId(long customId) {
        this.customId = customId;
    }

    public String getCustomFirstName() {
        return customFirstName;
    }

    public void setCustomFirstName(String customFirstName) {
        this.customFirstName = customFirstName;
    }

    public String getCustomLastName() {
        return customLastName;
    }

    public void setCustomLastName(String customLastName) {
        this.customLastName = customLastName;
    }

    public String getCustomCountry() {
        return customCountry;
    }

    public void setCustomCountry(String customCountry) {
        this.customCountry = customCountry;
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
        return "ReviewAccDTO{" +
               "customID=" + customId +
               ", customFirstName='"+ customFirstName + '\'' +
               ", customLastName='"+ customLastName +'\'' +
               ", customCountry='"+ customCountry +'\'' +
               ", rate=" + rate +
               ", comment='" + comment +'\'' +
               '}';
    }
}
