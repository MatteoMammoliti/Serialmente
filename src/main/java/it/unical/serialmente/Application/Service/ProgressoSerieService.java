package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import javafx.util.Pair;
import java.util.List;

public class ProgressoSerieService {

    private final ProgressoSerieDAOPostgres progressoSerieDao = new ProgressoSerieDAOPostgres(
            DBManager.getInstance().getConnection()
    );

//    private final TitoloService titoloService = new TitoloService();

    public List<Integer> getIdSerieTvInVisione() {
        return progressoSerieDao.getIdSerieTvInVisione(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

//    public Pair<Integer, Integer> getStatisticheSerieInVisione(Integer idSerieTV) throws Exception {
//
//        Integer numProgressivoStagione = progressoSerieDao.getNumeroProgressivoStagione(
//                SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                idSerieTV
//        );
//
//        Integer idEpisodioCorrente = progressoSerieDao.getIdEpisodioCorrente(
//                SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                idSerieTV
//        );
//
//        Integer numProgressivoEpisodio = progressoSerieDao.getNumeroProgressivoEpisodio(
//                SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                idSerieTV
//        );
//
//        Integer numEpisodiVisti = titoloService.
//        Integer sommaMinuti = titoloService.sommaMinutiEpisodiVisti(idSerieTV, numProgressivoStagione, idEpisodioCorrente);
//        return new Pair<>(sommaMinuti, numEpisodiVisti);
//    }

    public Pair<Integer, Integer> getStatisticheSerieInVisione(Integer idSerie) {
        return progressoSerieDao.getStatisticheSerieInVisione(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerie
        );
    }
}