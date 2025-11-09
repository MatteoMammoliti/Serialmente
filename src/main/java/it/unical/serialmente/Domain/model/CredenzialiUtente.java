package it.unical.serialmente.Domain.model;

public class CredenzialiUtente {
    private String password;
    private String email;
    private String domandaSicurezza;
    private String rispostaDomandaSicurezza;

    public CredenzialiUtente() {}
    public CredenzialiUtente(String password,String email,String domandaSicurezza,String rispostaDomandaSicurezza) {
        this.password=password;
        this.email=email;
        this.domandaSicurezza=domandaSicurezza;
        this.rispostaDomandaSicurezza=rispostaDomandaSicurezza;
    }

    //Setter

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {this.email = email;}
    public void setDomandaSicurezza(String domandaSicurezza) {this.domandaSicurezza = domandaSicurezza;}
    public void setRispostaDomandaSicurezza(String rispostaDomandaSicurezza) {this.rispostaDomandaSicurezza=rispostaDomandaSicurezza;}

    //Getter
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getDomandaSicurezza() {
        return domandaSicurezza;
    }
    public String getRispostaDomandaSicurezza() {
        return rispostaDomandaSicurezza;
    }
}
