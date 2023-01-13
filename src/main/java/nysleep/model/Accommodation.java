package nysleep.model;


import java.util.List;

public class Accommodation {
    private int id;
    private String name;
    private String neighborhood;
    private List<String> imagesURL;
    private int numBeds;
    private double rating;
    private int numReviews;
    private String propertyType;
    private int numRooms;
    private List<String> amenities;
    private double price;
    private Renter renter;
    private List<Reservation> reservations;

    public Accommodation(int id, String name, String neighborhood, List<String> imagesURL, int numBeds, double rating, int numReviews, String propertyType, int numRooms, List<String> amenities, double price, Renter renter, List<Reservation> reservations) {
        this.id = id;
        this.name = name;
        this.neighborhood = neighborhood;
        this.imagesURL = imagesURL;
        this.numBeds = numBeds;
        this.rating = rating;
        this.numReviews = numReviews;
        this.propertyType = propertyType;
        this.numRooms = numRooms;
        this.amenities = amenities;
        this.price = price;
        this.renter = renter;
        this.reservations = reservations;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getNeighborhood() {return neighborhood;}

    public void setNeighborhood(String neighborhood) {this.neighborhood = neighborhood;}

    public List<String> getImagesURL() {return imagesURL;}

    public void setImagesURL(List<String> imagesURL) {this.imagesURL = imagesURL;}

    public int getNumBeds() {return numBeds;}

    public void setNumBeds(int numBeds) {this.numBeds = numBeds;}

    public double getRating() {return rating;}

    public void setRating(double rating) {this.rating = rating;}

    public int getNumReviews() {return numReviews;}

    public void setNumReviews(int numReviews) {this.numReviews = numReviews;}

    public String getPropertyType() {return propertyType;}

    public void setPropertyType(String propertyType) {this.propertyType = propertyType;}

    public int getNumRooms() {return numRooms;}

    public void setNumRooms(int numRooms) {this.numRooms = numRooms;}

    public List<String> getAmenities() {return amenities;}

    public void setAmenities(List<String> amenities) {this.amenities = amenities;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}

    public Renter getRenter() {return renter;}

    public void setRenter(Renter renter) {this.renter = renter;}

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
