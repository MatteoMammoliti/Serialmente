package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.TechnicalServices.Persistence.model.Genere;
import it.unical.serialmente.TechnicalServices.Persistence.model.Piattaforma;
import it.unical.serialmente.TechnicalServices.Persistence.model.Titolo;

import java.util.HashSet;
import java.util.List;

public interface SelezioneTitoloDAO {
    List<Titolo> restituisciTitoliInLista(String nomeLista);
    boolean aggiungiTitoloInLista(Integer idTitolo,String nomeLista);
    boolean eliminaTitoloInLista(Integer idTitolo,String nomeLista);
    HashSet<Genere> restituisciGeneriVisionati();
}
