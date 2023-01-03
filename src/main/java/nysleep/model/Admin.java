package nysleep.model;

public class Admin extends RegisteredUser{
    private String title;

    public Admin(){

    }
    public Admin(String title) {
        this.title = title;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}
}
