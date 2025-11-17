package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;
import it.unical.serialmente.UI.Model.PagineNavigazione.ModelSezioneUtente;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SezioneUtenteService {
    private final SelezioneTitoloDAOPostgres selezioneTitoloDAOPostgres = new SelezioneTitoloDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final ProgressoSerieService progressoSerieService = new ProgressoSerieService();

    public List<Titolo> getTitoliInLista(String tipoLista,String tipoTitolo) throws SQLException {
        return selezioneTitoloDAOPostgres.restituisciTitoliInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                tipoLista,
                tipoTitolo);
    }

    public Integer getNumeroFilmVisionati(){
        return selezioneTitoloDAOPostgres.getNumeroFilmVisionati(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

    public List<Integer> getOreGiorniMesiVisionatiFilm(){
        return calcoloOreGiorniMesi(selezioneTitoloDAOPostgres.getMinutiVisioneFilm(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
                )
        );
    }

    public ModelSezioneUtente.StatisticheSerieTv getStatisticheSerieTv() throws Exception{
        List<Integer> listaSerieTvVisionate = selezioneTitoloDAOPostgres.getIdSerieVisionate(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );

        List<Integer> listaSerieTvInVisione = progressoSerieService.getIdSerieTvInVisione();

        Integer minutiTotali = 0;
        Integer episodiTotali = 0;

        for (Integer idserie : listaSerieTvInVisione) {
            Pair<Integer,Integer> minutiEpisodi = progressoSerieService.getStatisticheSerieInVisione(idserie);
            minutiTotali += minutiEpisodi.getKey();
            episodiTotali += minutiEpisodi.getValue();
        }

        for(Integer idSerie : listaSerieTvVisionate) {
            Pair<Integer,Integer> p = selezioneTitoloDAOPostgres.getStatistcheSerieTV(
                    SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    idSerie
            );
            episodiTotali += p.getKey();
            minutiTotali += p.getValue();
        }
        return new ModelSezioneUtente.StatisticheSerieTv(calcoloOreGiorniMesi(minutiTotali),episodiTotali);
    }

    private List<Integer> calcoloOreGiorniMesi(int minutiTotali){
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

    public void rendiTitoloPreferito(Titolo titolo) {
        selezioneTitoloDAOPostgres.aggiungiTitoloInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                "Preferiti",
                0,
                0);
    }

    public void togliTitoloPreferito(Titolo titolo) {
        selezioneTitoloDAOPostgres.eliminaTitoloInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                "Preferiti"
        );
    }
}
