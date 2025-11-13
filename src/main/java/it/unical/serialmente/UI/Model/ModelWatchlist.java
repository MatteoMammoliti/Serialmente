package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;

public class ModelWatchlist {
    private final ProgressoSerieDAOPostgres progressoSerieDAO = new ProgressoSerieDAOPostgres(DBManager.getInstance().getConnection());


    public Integer getNumeroStagione(Integer idSerie){
        return progressoSerieDAO.getNumeroProgressivoStagione(SessioneCorrente.getUtenteCorrente().getIdUtente(), idSerie);
    }
    public Integer getNumeroEpisodio(Integer idSerie){
        return progressoSerieDAO.getNumeroProgressivoEpisodio(SessioneCorrente.getUtenteCorrente().getIdUtente(), idSerie);
    }
    public String getNomeEpisodio(Integer idSerie){
        return progressoSerieDAO.getNomeEpisodio(SessioneCorrente.getUtenteCorrente().getIdUtente(), idSerie);
    }
}
