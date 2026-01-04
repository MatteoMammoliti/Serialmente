package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.SelezioneTitoloDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;
import it.unical.serialmente.UI.Model.PagineNavigazione.ModelSezioneUtente;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class SezioneUtenteService {

    private final SelezioneTitoloDAO selezioneTitoloDAOPostgres = new SelezioneTitoloDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final ProgressoSerieService progressoSerieService = new ProgressoSerieService();

    /**
     * Ritorna la lista specifica di titoli dell'utente.
     * @param tipoLista parametri accettati: Watchlist,Visionati,Preferiti
     * @param tipoTitolo parametri accettati:SerieTv,Film
     * @return Lista con i titoli selezionati
     */
    public List<Titolo> getTitoliInLista(String tipoLista,String tipoTitolo) {
        return selezioneTitoloDAOPostgres.restituisciTitoliInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                tipoLista,
                tipoTitolo);
    }

    /**
     * Ritorna il numero di film visionati dall'utente.
     * @return Numero di film visionati dall'utente.
     */
    public Integer getNumeroFilmVisionati(){
        return selezioneTitoloDAOPostgres.getNumeroFilmVisionati(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

    /**
     * ritorna un array contenente il numero di mesi,giorni e ore utilizzati dall'utente per visionare Film.
     * @return Array contentente numero di mesi, giorni e ore.
     */
    public List<Integer> getOreGiorniMesiVisionatiFilm(){
        return calcoloOreGiorniMesi(selezioneTitoloDAOPostgres.getMinutiVisioneFilm(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
                )
        );
    }

    /**
     * Ritorna il numero di mesi,giorni e ore utilizzati dall'utente per visionare SerieTv e il numero di episodi totali visionati.
     * @return
     */
    public ModelSezioneUtente.StatisticheSerieTv getStatisticheSerieTv(){
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

    /**
     * Dato un totale di minuti, calcola a quanti mesi,giorni e ore equivalgono.
     * @param minutiTotali Minuti totali impiegati dall'utente nella visione di titoli
     * @return restituisce una lista di interi con all'interno il numero di mesi, giorni e ore.
     */
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

    /**
     *
     * @param titolo da rendere preferito.
     */
    public void rendiTitoloPreferito(Titolo titolo) {
        selezioneTitoloDAOPostgres.aggiungiTitoloInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                "Preferiti",
                0,
                0
        );
    }

    /**
     * Rimuove il titolo selezionato dalla lista "Preferiti" dell'utente.
     * @param titolo
     */
    public void togliTitoloPreferito(Titolo titolo) {
        selezioneTitoloDAOPostgres.eliminaTitoloInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                "Preferiti"
        );
    }

    /**
     * Rimuove il titolo selezionato dalla lista "Visionati" dell'utente.
     * @param titolo
     */
    public void rimuoviTitoloDaVisionati(Titolo titolo) {
        selezioneTitoloDAOPostgres.eliminaTitoloInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                "Visionati"
        );
    }
}