package it.unical.serialmente.Application.Authentication;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

public class ValidazioneRegistrazione {

    public boolean validazioneEmail(String email){
        if(email == null || email.isEmpty()){
            return false;
        }

        try {
            InternetAddress indirizzoEmail = new InternetAddress(email);
            indirizzoEmail.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }

    }

    public boolean validazionePassword(String password){
        if(password == null || password.isEmpty()){
            return false;
        }
        return password.length() >= 8;
    }
}