package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.ProgressoSerieService;
import it.unical.serialmente.Application.Service.SezioneUtenteService;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelSezioneUtente {
    public record StatisticheSerieTv(List<Integer> durate,Integer episodiVisti){}
    private final SezioneUtenteService sezioneUtenteService = new SezioneUtenteService();


    public List<Titolo> getTitoliInLista(String tipoLista,String tipoTitolo) throws SQLException {
        return sezioneUtenteService.getTitoliInLista(tipoLista,tipoTitolo);
    }
    public Integer getNmeroFilmVisionati(){
        return sezioneUtenteService.getNumeroFilmVisionati();
    }
    public List<Integer> getOreGiorniMesiVisionatiFilm(){
        return sezioneUtenteService.getOreGiorniMesiVisionatiFilm();
    }

    public StatisticheSerieTv getStatisticheSerieTv() throws Exception {
        return sezioneUtenteService.getStatisticheSerieTv();
    }

    public void rendiTitoloPreferito(Titolo titolo) throws Exception {
        sezioneUtenteService.rendiTitoloPreferito(titolo);
    }

    public void togliTitoloDaiPreferiti(Titolo titolo) throws Exception {
        sezioneUtenteService.togliTitoloPreferito(titolo);
    }
}
