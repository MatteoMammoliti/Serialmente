package it.unical.serialmente.Domain.model;

public class CredenzialiUtente {
    private String password;
    private String email;

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
}