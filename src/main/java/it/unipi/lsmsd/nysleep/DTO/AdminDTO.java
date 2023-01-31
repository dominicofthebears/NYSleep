package it.unipi.lsmsd.nysleep.DTO;

import java.io.Serializable;

public class AdminDTO extends RegisteredUserDTO implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String title;

    public AdminDTO(int id, String firstName, String lastName, String email, String password, String title){
        super(id, firstName, lastName, email, password);
        this.title=title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
