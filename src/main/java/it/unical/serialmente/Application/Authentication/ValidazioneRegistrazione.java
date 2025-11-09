package it.unical.serialmente.Application.Authentication;

public class ValidazioneRegistrazione {

    public boolean validazioneEmail(String email){
        if(email == null || email.isEmpty()){
            return false;
        }

        String emailTrimmed = email.trim();
        String regexEmail =  "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
        return emailTrimmed.toLowerCase().matches(regexEmail);
    }

    public boolean validazionePassword(String password){
        if(password == null || password.isEmpty()){
            return false;
        }
        return password.length() >= 8;
    }
}
