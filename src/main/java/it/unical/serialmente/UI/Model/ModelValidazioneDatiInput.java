package it.unical.serialmente.UI.Model;

public class ModelValidazioneDatiInput {

   public static boolean validazioneEmail(String email){
       if(email==null || email.isEmpty()){
           return false;
       }
       String e= email.trim();
        String regex = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
        return e.toLowerCase().matches(regex);

    }

    public static boolean validazionePassword(String password){
        if(password==null || password.isEmpty()){
            return false;
        }
        return password.length()>=8;
    }
}
