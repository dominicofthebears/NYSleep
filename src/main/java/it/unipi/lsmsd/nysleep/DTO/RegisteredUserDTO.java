package it.unipi.lsmsd.nysleep.DTO;

public abstract class RegisteredUserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public RegisteredUserDTO(){}

    public RegisteredUserDTO(int id, String firstName, String lastName, String email, String password){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.password=password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}