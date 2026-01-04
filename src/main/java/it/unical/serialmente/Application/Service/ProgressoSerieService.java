package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.ProgressoSerieDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import javafx.util.Pair;
import java.util.List;

public class ProgressoSerieService {

    private final ProgressoSerieDAO progressoSerieDao = new ProgressoSerieDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    /**
     * Restituisce gli id delle serie che attualmente sono nella Watchlist.
     */
    public List<Integer> getIdSerieTvInVisione() {
        return progressoSerieDao.getIdSerieTvInVisione(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

    /**
     * Restituisce la coppia minuti visti e episodi visti di una serieTv.
     * @param idSerie Id della serie da cui estrapolre le informazioni.
     */
    public Pair<Integer, Integer> getStatisticheSerieInVisione(Integer idSerie) {
        return progressoSerieDao.getStatisticheSerieInVisione(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerie
        );
    }
}