package it.unical.serialmente.Application.Service;

import it.unical.serialmente.TechnicalServices.API.TMDbAPI;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.TitoloDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.UtenteDAOPostgres;

public class WatchlistService {

    private final TMDbAPI tmDbAPI = new  TMDbAPI();
    private final TitoloDAOPostgres titoloDao = new TitoloDAOPostgres(DBManager.getInstance().getConnection());
    private final UtenteDAOPostgres  utenteDao = new UtenteDAOPostgres(DBManager.getInstance().getConnection());

    public void inserisciTitoloInWatchlist() {}
    public void rimuoviTitoloDallaWatchlist() {}
    public void rendiTitoloVisionato() {}
}
