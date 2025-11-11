package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;

import java.sql.SQLException;
import java.util.List;

public class ModelSezioneUtente {
    private final SelezioneTitoloDAOPostgres selezioneTitoloDAO = new SelezioneTitoloDAOPostgres(DBManager.getInstance().getConnection());



    public List<Titolo> getTitoliInLista(String tipoLista,String tipoTitolo) throws SQLException {
        return selezioneTitoloDAO.restituisciTitoliInLista(SessioneCorrente.getUtenteCorrente().getIdUtente(), tipoLista,tipoTitolo);
    }
}
