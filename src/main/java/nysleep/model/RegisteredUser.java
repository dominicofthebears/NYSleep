package nysleep.model;

public abstract class RegisteredUser {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String url_prof_pic;
    private String type;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getUrl_prof_pic(){
        return url_prof_pic;
    }

    public void setUrl_prof_pic(String url_prof_pic){
        this.url_prof_pic=url_prof_pic;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type=type;
    }

    @Override
    public String toString(){
        return "RegisteredUser{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", url_prof_pic='" + url_prof_pic + '\'' +
               ", type='" + type +
               '}';
    }
}
