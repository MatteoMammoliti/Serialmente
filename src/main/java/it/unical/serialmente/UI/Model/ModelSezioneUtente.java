package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.ProgressoSerieService;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelSezioneUtente {
    private final SelezioneTitoloDAOPostgres selezioneTitoloDAO = new SelezioneTitoloDAOPostgres(DBManager.getInstance().getConnection());
    private final ProgressoSerieService progressoSerieService = new ProgressoSerieService();
    public record StatisticheSerieTv(List<Integer> durate,Integer episodiVisti){}


    public List<Titolo> getTitoliInLista(String tipoLista,String tipoTitolo) throws SQLException {
        return selezioneTitoloDAO.restituisciTitoliInLista(SessioneCorrente.getUtenteCorrente().getIdUtente(), tipoLista,tipoTitolo);
    }
    public Integer getNmeroFilmVisionati(){
        return selezioneTitoloDAO.getNumeroFilmVisionati(SessioneCorrente.getUtenteCorrente().getIdUtente());
    }
    public List<Integer> getOreGiorniMesiVisionatiFilm(){
        return calcoloOreGiorniMesi(selezioneTitoloDAO.getMinutiVisioneFilm(SessioneCorrente.getUtenteCorrente().getIdUtente()));
    }

    public StatisticheSerieTv getStatisticheSerieTv() throws Exception {
        List<Integer> listaSerieTvVisionate = selezioneTitoloDAO.getIdSerieVisionate(SessioneCorrente.getUtenteCorrente().getIdUtente());
        Integer minutiTotali=0;
        Integer episodiTotali=0;
        for (Integer idserie : listaSerieTvVisionate) {
            Pair<Integer,Integer> minutiEpisodi=progressoSerieService.getStatisticheEpisodio(idserie);
            minutiTotali+=minutiEpisodi.getKey();
            episodiTotali+=minutiEpisodi.getValue();
        }
        return new StatisticheSerieTv(calcoloOreGiorniMesi(minutiTotali),episodiTotali);



    }
    public List<Integer> calcoloOreGiorniMesi(int minutiTotali){
        List<Integer> oreGiorniMesiVisionati=new ArrayList<>();
        int minutiPerMese = 43200;
        int minutiPerGiorno = 1440;
        int minutiPerOra = 60;

        int mesi = minutiTotali / minutiPerMese;
        int resto = minutiTotali % minutiPerMese;

        int giorni = resto / minutiPerGiorno;
        resto %= minutiPerGiorno;

        int ore = resto / minutiPerOra;

        oreGiorniMesiVisionati.add(mesi);
        oreGiorniMesiVisionati.add(giorni);
        oreGiorniMesiVisionati.add(ore);
        return oreGiorniMesiVisionati;

    }
}
