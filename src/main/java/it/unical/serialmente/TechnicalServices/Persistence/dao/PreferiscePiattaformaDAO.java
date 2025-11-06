package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Piattaforma;

import java.util.List;

public interface PreferiscePiattaformaDAO {
    List<Piattaforma> getPiattaformePreferiteUtente(Integer idUtente);
    boolean aggiungiPiattaformaPreferitaUtente(Integer idUtente,Integer idPiattaforma);
    boolean rimuoviPiattaformaPreferitaUtente(Integer idUtente,Integer idPiattaforma);
}
