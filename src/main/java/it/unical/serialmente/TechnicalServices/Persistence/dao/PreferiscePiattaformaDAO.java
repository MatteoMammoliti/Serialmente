package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Piattaforma;

import java.util.List;

public interface PreferiscePiattaformaDAO {
    List<Piattaforma> getPiattaformePreferiteUtente(Integer idUtente) throws Exception;
    void aggiungiPiattaformaPreferitaUtente(Integer idUtente, Integer idPiattaforma) throws Exception;
    void rimuoviPiattaformaPreferitaUtente(Integer idUtente, Integer idPiattaforma) throws Exception;
}
