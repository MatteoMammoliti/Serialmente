package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Utente;

public interface UtenteDAO {

    boolean inserisciUtente(Utente utente);
    boolean cancellaUtente(Integer idUtente);
    boolean modificaNomeUtente(Integer idUtente,String nuovoNome);

}
