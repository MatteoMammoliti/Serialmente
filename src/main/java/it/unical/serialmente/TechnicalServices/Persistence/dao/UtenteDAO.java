package it.unical.serialmente.TechnicalServices.Persistence.dao;

public interface UtenteDAO {

    int inserisciUtente(String utente);
    boolean cancellaUtente(Integer idUtente);
    boolean modificaNomeUtente(Integer idUtente,String nuovoNome);

}
