package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Piattaforma;

import java.util.List;

public interface PiattaformaDAO {
    boolean aggiungiPiattaforma(Integer idPiattaforma,String nomePiattaforma);
    boolean rimuoviPiattaforma(Integer idPiattaforma);
    List<Piattaforma> getListaPiattaforma();
}
