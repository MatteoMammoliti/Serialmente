package it.unical.serialmente.UI.Model.ModelAutenticazione;

import it.unical.serialmente.Application.Authentication.ValidazioneRegistrazione;
import it.unical.serialmente.Application.Service.UtenteService;


public class ModelCambioPassword {
    private final UtenteService utenteService = new UtenteService();
    private final ValidazioneRegistrazione validazioneRegistrazione = new ValidazioneRegistrazione();

    public boolean controlloEmailEsistente(String Email){
        return utenteService.controlloEmailEsistente(Email);
    }
    public String getDomandaSicurezzaUtente(String email){
        return utenteService.getDomandaSicurezzaUtente(email);
    }
    public boolean controlloDomandaSicurezza(String email,String risposta){
        return utenteService.controlloRispostaDomandaSicurezza(email,risposta);
    }
    public boolean verificaIdonietaPassword(String pw){
        return validazioneRegistrazione.validazionePassword(pw);
    }
    public boolean cambiaPassword(String email,String password){
        return utenteService.cambiaPassword(email,password);
    }

}
