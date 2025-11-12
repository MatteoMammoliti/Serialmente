package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.API.TMDbAPI;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import javafx.util.Pair;

public class ProgressoSerieService {

    private final ProgressoSerieDAOPostgres progressoSerieDao = new ProgressoSerieDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final TitoloService titoloService = new TitoloService();

    private final TMDbAPI tmdb = new TMDbAPI();

    public Pair<Integer, Integer> getStatisticheEpisodio(Integer idSerieTV) throws Exception {
        Integer numProgressivoStagione = progressoSerieDao.getNumeroProgressivoStagione(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerieTV
        );

        Integer numProgressivoEpisodio = progressoSerieDao.getNumeroProgressivoEpisodio(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerieTV
        );

        Integer numEpisodiPerStagione = titoloService.getNumeroEpisodiStagione(idSerieTV);
        Integer numEpisodiVisti = titoloService.sommaEpisodiVisti(idSerieTV, numEpisodiPerStagione) + numProgressivoEpisodio - 1;
        Integer sommaMinuti = titoloService.sommaMinutiEpisodiVisti(idSerieTV, numProgressivoStagione, numProgressivoEpisodio);
        return new Pair<>(sommaMinuti, numEpisodiVisti);
    }
}
