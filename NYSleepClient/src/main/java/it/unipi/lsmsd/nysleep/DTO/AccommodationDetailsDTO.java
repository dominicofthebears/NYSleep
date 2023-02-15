package it.unipi.lsmsd.nysleep.DTO;

import java.io.Serializable;
import java.util.List;

public class AccommodationDetailsDTO implements Serializable {
    private int id;
    private String name;
    private String neighborhood;
    private List<String> imagesURL ;
    private int numBeds;
    private double rating;
    private List<String> amenities;
    private double price;
    private RenterDetailsDTO renterDetailsDTO;
    private String propertyType;
    private int numRooms;

    //property type, num rooms

    public AccommodationDetailsDTO(int id, String name, String neighborhood, double rating, List<String> imagesURL, int numBeds, List<String> amenities, double price, RenterDetailsDTO renterDetailsDTO, String type, int num_rooms) {
        this.id = id;
        this.name = name;
        this.neighborhood = neighborhood;
        this.rating = rating;
        this.imagesURL = imagesURL;
        this.numBeds = numBeds;
        this.amenities = amenities;
        this.price = price;
        this.renterDetailsDTO = renterDetailsDTO;
        this.propertyType = type;
        this.numRooms = num_rooms;
    }

    public AccommodationDetailsDTO() {

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

    public RenterDetailsDTO getRenterDetailsDTO() {
        return renterDetailsDTO;
    }

    public void setRenterDetailsDTO(RenterDetailsDTO renterDetailsDTO) {
        this.renterDetailsDTO = renterDetailsDTO;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String type) {
        this.propertyType = type;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }
}