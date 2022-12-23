package nysleep.DTO;

public class AccommodationDTO {
    private long id;
    private String name;
    private String neighborhood;
    private float rating;
    private String mainPicUrl;

    public AccommodationDTO(long id, String name, String neighborhood, float rating, String mainPicUrl) {
        this.id = id;
        this.name = name;
        this.neighborhood = neighborhood;
        this.rating = rating;
        this.mainPicUrl = mainPicUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
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
