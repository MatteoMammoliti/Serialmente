package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.CredenzialiUtente;
import it.unical.serialmente.Domain.model.Utente;

public interface CredenzialiUtenteDAO {

    boolean insertCredenzialiUtente(String email,String password,String domandaSicurezza,String rispostaDomandaSicurezza,Integer idUtente);
    boolean deleteCredenzialiUtente(Integer idUtente);
    boolean cercaEmail(String email);
    boolean validaCredenzialiUtente(String email, String password);
    Integer cercaIdUtente(String email);

}
