package it.unical.serialmente.Application.Authentication;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

public class ValidazioneRegistrazione {

    /**
     * Verifica se l'email rispetta gli standard RFC
     * @param email Email dell'utente
     * @return valore booleano.
     */
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

    /**
     * Verifica se la password ha almeno 8 caratteri.
     * @param password password dell'utente.
     * @return valore booleano di conferma.
     */
    public boolean validazionePassword(String password){
        if(password == null || password.isEmpty()){
            return false;
        }
        return password.length() >= 8;
    }
}