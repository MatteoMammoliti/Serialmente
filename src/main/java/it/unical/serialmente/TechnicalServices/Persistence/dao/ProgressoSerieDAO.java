package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.ContenitoreDatiProgressoSerie;
import it.unical.serialmente.Domain.model.Episodio;
import it.unical.serialmente.Domain.model.Stagione;
import it.unical.serialmente.Domain.model.Titolo;

public interface ProgressoSerieDAO {
    boolean cambiaEpisodioCorrente(Integer idUtente, Integer idSerieTV, String descrizioneEpisodio, Integer durataEpisodio,  Integer idEpisodioProssimo, Integer numeroProgressivoEpisodio);
    boolean cambiaStagioneCorrente(Integer idUtente, Integer idSerieTV, Integer annoPubblicazione, Integer idStagioneProssima, Integer numeroProgressivoStagione);
    boolean creaIstanzaProgressoSerie(Integer idUtente, Integer idTitolo, Integer idEpisodio, Integer idStagione, Integer annoPubblicazioneStagione, String descrizioneEpisodio, Integer durataMinuti, Integer numeroProgressivoStagione, Integer numeroProgressivoEpisodio);
    Integer getIdEpisodioCorrente(Integer idUtente,Integer idSerieTV);
    ContenitoreDatiProgressoSerie getDatiCorrenti(Integer idUtente, Titolo titolo);
    Integer getIdStagioneCorrente(Integer idUtente, Integer idSerieTV);
    Integer getNumeroProgressivoEpisodio(Integer idUtente, Integer idSerieTV);
    Integer getNumeroProgressivoStagione(Integer idUtente, Integer idSerieTV);
    String getNomeEpisodio(Integer idUtente, Integer idSerieTV);
}
