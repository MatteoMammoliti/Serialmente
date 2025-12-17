package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Genere;

import java.util.List;

public interface PreferisceGenereDAO {
    List<Genere>  getGeneriPreferitiUtente(Integer idUtente);
    void aggiungiGenerePreferitoUtente(Integer idUtente, Integer idGenere);
    void rimuoviGenerePreferitoUtente(Integer idUtente, Integer idGenere);

}
