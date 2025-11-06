package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;

import java.sql.SQLException;
import java.util.List;


public interface TitoloDAO {
    Titolo restituisciTitoloPerId(Integer idTitolo);
    boolean aggiungiTitolo(Titolo titolo) throws SQLException;
    boolean rimuoviTitolo(Titolo titolo);
    List<Titolo> restituisciTitoloPerNome(String nomeTitolo);
    List<Genere> restituisciGeneriTitolo(Integer idTitolo);


}
