package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.TechnicalServices.Persistence.model.CredenzialiUtente;
import it.unical.serialmente.TechnicalServices.Persistence.model.Utente;

public interface CredenzialiUtenteDAO {

    boolean insertCredenzialiUtente(CredenzialiUtente credenzialiUtente,Integer idUtente);
    CredenzialiUtente getCredenzialiUtente(Utente utente);
    boolean deleteCredenzialiUtente(Integer idUtente);
    boolean cercaEmail(String email);
    boolean validaCredenzialiUtente(String email, String password);
    Integer cercaIdUtente(String email);

}
