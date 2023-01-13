package nysleep.DTO;

import java.util.LinkedList;
import java.util.List;

public class AccommodationDetailsDTO {
    private int id;
    private String name;
    private String neighborhood;

    private LinkedList<String> imagesURL ;

    private int numBeds;
    private double rating;

    private LinkedList<String> amenities;

    private double price;
    private String renterFirstName;
    private String renterLastName;
    private String renterWorkEmail;
    private String renterPhone;

    public AccommodationDetailsDTO(int id, String name, String neighborhood, double rating, List<String> imagesURL, int numBeds, List<String> amenities, double price, String renterFirstName, String renterLastName, String renterWorkEmail, String renterPhone) {
        this.id = id;
        this.name = name;
        this.neighborhood = neighborhood;
        this.rating = rating;
        this.imagesURL = (LinkedList<String>) imagesURL;
        this.numBeds = numBeds;
        this.amenities = (LinkedList<String>)amenities;
        this.price = price;
        this.renterFirstName = renterFirstName;
        this.renterLastName = renterLastName;
        this.renterWorkEmail = renterWorkEmail;
        this.renterPhone = renterPhone;
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

    public LinkedList<String> getImagesURL() {
        return imagesURL;
    }

    public void setImagesURL(LinkedList<String> imagesURL) {
        this.imagesURL = imagesURL;
    }

    public int getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }

    public LinkedList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(LinkedList<String> amenities) {
        this.amenities = amenities;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRenterFirstName() {
        return renterFirstName;
    }

    public void setRenterFirstName(String renterFirstName) {
        this.renterFirstName = renterFirstName;
    }

    public String getRenterLastName() {
        return renterLastName;
    }

    public void setRenterLastName(String renterLastName) {
        this.renterLastName = renterLastName;
    }

    public String getRenterWorkEmail() {
        return renterWorkEmail;
    }

    public void setRenterWorkEmail(String renterWorkEmail) {
        this.renterWorkEmail = renterWorkEmail;
    }

    public String getRenterPhone() {
        return renterPhone;
    }

    public void setRenterPhone(String renterPhone) {
        this.renterPhone = renterPhone;
    }
}