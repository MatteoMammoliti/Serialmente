package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.UtenteService;
import it.unical.serialmente.Domain.model.CredenzialiUtente;
import it.unical.serialmente.Domain.model.Utente;

import java.sql.SQLException;

public class ModelRegistrazione {
    UtenteService utenteService=new UtenteService();

    public boolean registraUtente(String nome,
                                  String email,
                                  String password,
                                  String domandaSicurezza,
                                  String rispostaDomandaSicurezza) throws SQLException {

        return utenteService.registraUtente(nome,email,password,domandaSicurezza,rispostaDomandaSicurezza);
    }
}
