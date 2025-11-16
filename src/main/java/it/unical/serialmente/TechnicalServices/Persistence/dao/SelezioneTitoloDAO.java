package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.List;

public interface SelezioneTitoloDAO {
    List<Titolo> restituisciTitoliInLista(Integer idUtente,String nomeLista,String tipologiaTitolo);
    boolean aggiungiTitoloInLista(Integer idUtente,Integer idTitolo,String nomeLista, Integer minuti_visti, Integer numero_episodi_visti);
    boolean eliminaTitoloInLista(Integer idUtente,Integer idTitolo,String nomeLista);
    HashSet<Genere> restituisciGeneriVisionati(Integer idUtente);
    boolean controlloTitoloInListe(Integer idTitolo);
    Integer getNumeroFilmVisionati(Integer idUtente);
    Integer getMinutiVisioneFilm(Integer idUtente);
    List<Integer> getIdSerieVisionate(Integer idUtente);
    boolean controlloTitoloInListeUtente(Integer idUtente,Integer idTitolo);
    public Pair<Integer, Integer> getStatistcheSerieTV(Integer idUtente, Integer idSerie);
}
