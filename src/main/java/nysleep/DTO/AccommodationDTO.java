package nysleep.DTO;

public class AccommodationDTO {
    private int id;
    private String name;
    private String neighborhood;
    private double rating;
    private String mainPicUrl;

    public AccommodationDTO(){}

    public AccommodationDTO(int id, String name, String neighborhood, double rating, String mainPicUrl) {
        this.id = id;
        this.name = name;
        this.neighborhood = neighborhood;
        this.rating = rating;
        this.mainPicUrl = mainPicUrl;
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
               '}';
    }
}
