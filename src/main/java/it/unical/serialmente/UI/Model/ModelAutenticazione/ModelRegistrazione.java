package it.unical.serialmente.UI.Model.ModelAutenticazione;

import it.unical.serialmente.Application.Service.UtenteService;

import java.sql.SQLException;

public class ModelRegistrazione {
    private final UtenteService utenteService=new UtenteService();

    public boolean registraUtente(String nome,
                                  String email,
                                  String password,
                                  String domandaSicurezza,
                                  String rispostaDomandaSicurezza) throws SQLException {

        return utenteService.registraUtente(nome,email,password,domandaSicurezza,rispostaDomandaSicurezza);
    }
}
