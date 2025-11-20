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

    public List<Integer> getIdSerieTvInVisione() {
        return progressoSerieDao.getIdSerieTvInVisione(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

    public Pair<Integer, Integer> getStatisticheSerieInVisione(Integer idSerie) {
        return progressoSerieDao.getStatisticheSerieInVisione(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerie
        );
    }
}