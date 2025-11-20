package it.unical.serialmente.UI.Model.ModelAutenticazione;

import it.unical.serialmente.Application.Service.UtenteService;
import it.unical.serialmente.Domain.model.Utente;

public class ModelLogin {

    private final UtenteService utenteService =  new UtenteService();

    public Utente autenticazioneUtente(String email, String password) {
        return utenteService.loginUtente(email, password);
    }

    public boolean isPrimoAccesso(Utente u) {
        return utenteService.isPrimoAcceso(u);
    }
}
