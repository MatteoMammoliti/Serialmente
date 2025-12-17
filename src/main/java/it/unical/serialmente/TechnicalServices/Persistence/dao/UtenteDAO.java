package it.unical.serialmente.TechnicalServices.Persistence.dao;

public interface UtenteDAO {

    int inserisciUtente(String utente);
    boolean isPrimoAccesso(Integer idUtente);
    void impostaPrimoAccesso(Integer idUtente);
    String getNomeUtente(Integer idUtente);
}
