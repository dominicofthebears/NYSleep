package it.unipi.lsmsd.nysleep.DTO;

import java.util.List;

public class ModifiedAccDTO {
    private int id;
    private String name;
    private List<String> imagesURL;
    private int numBeds;
    private int numReviews;
    private int numRooms;
    private java.util.List<String> amenities;
    private double price;

    public ModifiedAccDTO(int id,
                          String name,
                          List<String> imagesURL,
                          int numBeds,
                          int numReviews,
                          int numRooms,
                          List<String> amenities){
        this.id = id;
        this.name = name;
        this.imagesURL = imagesURL;
        this.numBeds = numBeds;
        this.numReviews = numReviews;
        this.numRooms = numRooms;
        this.amenities = amenities;
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

    public List<String> getImagesURL() {
        return imagesURL;
    }

    public void setImagesURL(List<String> imagesURL) {
        this.imagesURL = imagesURL;
    }

    public int getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
