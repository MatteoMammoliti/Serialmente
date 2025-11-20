package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Piattaforma;

import java.util.List;

public interface PreferiscePiattaformaDAO {
    List<Piattaforma> getPiattaformePreferiteUtente(Integer idUtente) throws Exception;
    boolean aggiungiPiattaformaPreferitaUtente(Integer idUtente,Integer idPiattaforma) throws Exception;
    boolean rimuoviPiattaformaPreferitaUtente(Integer idUtente,Integer idPiattaforma) throws Exception;
}
