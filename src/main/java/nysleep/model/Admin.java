package nysleep.model;

public class Admin extends RegisteredUser{
    private String title;

    public Admin(){

    }
    public Admin(int id,String firstName,String lastName,String email,String password,String url_profile_pic,String type,String title) {
        super.setId(id);
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        super.setUrl_prof_pic(url_profile_pic);
        super.setPassword(password);
        super.setType(type);
        this.title = title;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}
}
