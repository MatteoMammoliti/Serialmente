package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.TechnicalServices.Persistence.model.Genere;

import java.util.List;

public interface PreferisceGenereDAO {
    List<Genere>  getGeneriPreferitiUtente(Integer idUtente);
    boolean aggiungiGenerePreferitoUtente(Integer idUtente,Integer idGenere);
    boolean rimuoviGenerePreferitoUtente(Integer idUtente,Integer idGenere);

}
