package it.unical.serialmente.TechnicalServices.Persistence.dao;

import java.util.List;

public interface ProgressoSerieDAO {
    void cambiaEpisodioCorrente(Integer idUtente, Integer idSerieTV, String descrizioneEpisodio, Integer durataEpisodio, Integer idEpisodioProssimo, Integer numeroProgressivoEpisodio);
    void cambiaStagioneCorrente(Integer idUtente, Integer idSerieTV, Integer idStagioneProssima, Integer numeroProgressivoStagione);
    boolean creaIstanzaProgressoSerie(Integer idUtente, Integer idTitolo, Integer idEpisodio, Integer idStagione, String descrizioneEpisodio, Integer durataMinuti, Integer numeroProgressivoStagione, Integer numeroProgressivoEpisodio);
    Integer getIdEpisodioCorrente(Integer idUtente,Integer idSerieTV);
    // ContenitoreDatiProgressoSerie getDatiCorrenti(Integer idUtente, Titolo titolo);
    Integer getIdStagioneCorrente(Integer idUtente, Integer idSerieTV);
    Integer getNumeroProgressivoEpisodio(Integer idUtente, Integer idSerieTV);
    Integer getNumeroProgressivoStagione(Integer idUtente, Integer idSerieTV);
    String getNomeEpisodio(Integer idUtente, Integer idSerieTV);
    boolean eliminaSerieDaProgressioSerie(Integer idUtente, Integer idSerieTV);
    List<Integer> getIdSerieTvInVisione(Integer idUtente);
    // List<Integer> getStagioniInCorso(Integer idUtente);
    boolean controlloSerieTvInCorso(Integer idUtente, Integer idSerieTV);
}
