package it.unical.serialmente.TechnicalServices.Persistence.dao;

import javafx.util.Pair;

import java.sql.SQLException;
import java.util.List;

public interface ProgressoSerieDAO {
    void avanzaEpisodio(Integer idUtente, Integer idSerieTV, Integer durataEpisodio, Integer idEpisodioProssimo);
    void avanzaEpisodioEstagione(Integer idUtente,
                                 Integer idSerieTV,
                                 Integer durataEpisodio,
                                 Integer idEpisodioProssimo,
                                 Integer idProssimaStagione
    ) throws SQLException;

    boolean creaIstanzaProgressoSerie(Integer idUtente,
                                      Integer idTitolo,
                                      Integer idEpisodio,
                                      Integer idStagione,
                                      Integer durataMinuti,
                                      Integer numeroProgressivoStagione,
                                      Integer numeroProgressivoEpisodio
    );

    Integer getIdEpisodioCorrente(Integer idUtente,Integer idSerieTV);
    Integer getIdStagioneCorrente(Integer idUtente, Integer idSerieTV);
    Integer getNumeroProgressivoEpisodio(Integer idUtente, Integer idSerieTV);
    Integer getNumeroProgressivoStagione(Integer idUtente, Integer idSerieTV);
    boolean eliminaSerieDaProgressioSerie(Integer idUtente, Integer idSerieTV);
    List<Integer> getIdSerieTvInVisione(Integer idUtente);
    boolean controlloSerieTvInCorso(Integer idUtente, Integer idSerieTV);
    Pair<Integer, Integer> getStatisticheSerieInVisione(Integer idUtente, Integer idSerie);
}