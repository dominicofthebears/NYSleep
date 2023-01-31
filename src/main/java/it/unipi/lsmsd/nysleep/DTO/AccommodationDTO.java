package it.unipi.lsmsd.nysleep.DTO;

import java.io.Serializable;

public class AccommodationDTO implements Serializable {
    private int id;
    private String name;
    private String neighborhood;
    private double rating;
    private String mainPicUrl;
    private double price;

    public AccommodationDTO(){}

    public AccommodationDTO(int id, String name, String neighborhood, double rating, String mainPicUrl, double price) {
        this.id = id;
        this.name = name;
        this.neighborhood = neighborhood;
        this.rating = rating;
        this.mainPicUrl = mainPicUrl;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getMainPicUrl() {
        return mainPicUrl;
    }

    public void setMainPicUrl(String mainPicUrl) {
        this.mainPicUrl = mainPicUrl;
    }

    @Override
    public String toString(){
        return "AccommodationDTO{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", neighborhood='" + neighborhood + '\'' +
               ", rating=" + rating +
               ", mainPicUrl='" + mainPicUrl + '\'' +
               ", price=" + price +
                '}';
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
