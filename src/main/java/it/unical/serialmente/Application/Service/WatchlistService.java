package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Application.Mapper.Mapper;
import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.API.TMDbHttpClient;
import it.unical.serialmente.TechnicalServices.API.TMDbRequest;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.TitoloDAOPostgres;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.List;

public class WatchlistService {

    private final TMDbRequest tmdbRequest = new TMDbRequest();
    private final TMDbHttpClient tmdbHttpClient = new TMDbHttpClient();
    private final Mapper mapper = new Mapper();

    private final TitoloDAOPostgres titoloDao = new TitoloDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final TitoloService titoloService = new TitoloService();

    private final SelezioneTitoloDAOPostgres selezioneTitoloDao = new SelezioneTitoloDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final ProgressoSerieDAOPostgres  progressoDao = new ProgressoSerieDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    public void inserisciTitoloInWatchlist(Titolo titolo) throws SQLException {
        try {

            DBManager.getInstance().getConnection().setAutoCommit(false);

            boolean inserimentoConSuccesso = false;
            boolean primoInserimento = true;
            if(titoloDao.restituisciTitoloPerId(titolo.getIdTitolo()) == null) {
                primoInserimento=titoloDao.aggiungiTitolo(titolo);
            }

            if(!primoInserimento) {
                DBManager.getInstance().getConnection().rollback();
                return;
            }

            inserimentoConSuccesso = selezioneTitoloDao.aggiungiTitoloInLista(
                    SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo(),
                    "Watchlist",
                    0,
                    0
            );

            if(!inserimentoConSuccesso) {
                DBManager.getInstance().getConnection().rollback();

                return;
            }

            if(titolo.getTipologia().equals("SerieTv")) {

                int idPrimaStagione = mapper.parseIdProssimaStagione(
                        tmdbHttpClient.richiesta(
                            tmdbRequest.getTitolo(
                                    titolo.getIdTitolo(),
                                    "tv"
                            )
                        ),
                        0
                ).getKey();

                Pair<Integer, Integer> p = mapper.parseIdProssimoEpisodio(
                        tmdbHttpClient.richiesta(
                                tmdbRequest.getEpisodiDaStagione(
                                        1,
                                        titolo.getIdTitolo()
                                )
                        ),
                        0
                );

                inserimentoConSuccesso = progressoDao.creaIstanzaProgressoSerie(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo(),
                        p.getKey(),
                        idPrimaStagione,
                        p.getValue(),
                        1,
                        1
                );
            }

            if(!inserimentoConSuccesso) {
                DBManager.getInstance().getConnection().rollback();
                return;
            }

            DBManager.getInstance().getConnection().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void rimuoviFilmDallaWatchlist(Titolo titolo) throws SQLException {

        try {
            DBManager.getInstance().getConnection().setAutoCommit(false);
            boolean eliminazione = selezioneTitoloDao.eliminaTitoloInLista(SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo(),
                    "Watchlist"
            );

            if(!eliminazione) {
                DBManager.getInstance().getConnection().rollback();
                return;
            }

            if(!selezioneTitoloDao.controlloTitoloInListe(titolo.getIdTitolo())) {
                eliminazione = titoloDao.rimuoviTitolo(titolo);
            }

            if(!eliminazione) {
                DBManager.getInstance().getConnection().rollback();
                return;
            }

            DBManager.getInstance().getConnection().commit();

        } finally {
            DBManager.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void rimuoviSerieWatchlist(Titolo titolo) throws SQLException {

        DBManager.getInstance().getConnection().setAutoCommit(false);

        boolean successo = selezioneTitoloDao.eliminaTitoloInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                "Watchlist"
        );

        if(!successo) {
            DBManager.getInstance().getConnection().rollback();
            return;
        }

        successo= progressoDao.eliminaSerieDaProgressioSerie(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo()
        );

        if (!successo) {
            DBManager.getInstance().getConnection().rollback();
            return;
        }
        DBManager.getInstance().getConnection().commit();
    }

    public void rendiTitoloVisionato(Titolo titolo) throws Exception {

        if(titolo.getTipologia().equals("Film")) {
            selezioneTitoloDao.aggiungiTitoloInLista(SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo(),
                    "Visionati",
                    ((Film) titolo).getDurataMinuti(),
                    0
            );
            return;
        }

        rendiSerieVisionata(((SerieTV) titolo));
    }

    public int getIdProssimaStagione(Titolo titolo) throws Exception {

        String url = tmdbRequest.getTitolo(
                titolo.getIdTitolo(),
                "tv"
        );

        String infoSerie = tmdbHttpClient.richiesta(url);
        return mapper.parseIdProssimaStagione(
                infoSerie,
                progressoDao.getIdStagioneCorrente(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo()
                )
        ).getKey();
    }

    public void rendiEpisodioVisionato(Titolo titolo) throws Exception {

        String urlProssimoEpisodio = tmdbRequest.getEpisodiDaStagione(
            progressoDao.getNumeroProgressivoStagione(
                    SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo()
            ),
            titolo.getIdTitolo()
        );

        String RispostaIdProssimoEpisodio = tmdbHttpClient.richiesta(urlProssimoEpisodio);
        Pair<Integer, Integer> p = mapper.parseIdProssimoEpisodio(
                RispostaIdProssimoEpisodio,
                progressoDao.getIdEpisodioCorrente(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo()
                )
        );

        int idProssimoEpisodio = p.getKey();
        int durataProssimoEpisodio = p.getValue();

        int idProssimaStagione = 0;

        if(idProssimoEpisodio == 0) {
            idProssimaStagione = getIdProssimaStagione(titolo);

            progressoDao.avanzaEpisodioEstagione(
                    SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo(),
                    durataProssimoEpisodio,
                    idProssimoEpisodio,
                    idProssimaStagione
            );
            return;
        }

        progressoDao.avanzaEpisodio(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                durataProssimoEpisodio,
                idProssimoEpisodio
        );
    }

    public List<Titolo> restituisciTitoliInWatchlist() throws Exception {

        List<Titolo> titoliSerie = selezioneTitoloDao.restituisciTitoliInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                "Watchlist","SerieTv"
        );
        List<Titolo> titoliFilm = selezioneTitoloDao.restituisciTitoliInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                "Watchlist","Film"
        );
        titoliSerie.addAll(titoliFilm);
        return titoliSerie;
    }

    public Integer getNumeroStagione(Integer idSerie){
        return progressoDao.getNumeroProgressivoStagione(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerie);
    }

    public Integer getNumeroEpisodio(Integer idSerie){
        return  progressoDao.getNumeroProgressivoEpisodio(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerie);
    }

    public boolean controlloPresenzaTitoloInListe(Integer idTitolo){
        return selezioneTitoloDao.controlloTitoloInListeUtente(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idTitolo);
    }

    public boolean controlloPresenzaSerieTvInListe(Integer idSerie){
        return selezioneTitoloDao.controlloTitoloInListeUtente(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerie
        ) ||
                progressoDao.controlloSerieTvInCorso(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        idSerie
                );
    }

    private void rendiSerieVisionata(SerieTV serie) throws Exception {

        titoloService.setDati(serie);

        int totaleMinuti = 0;
        int totaleEpisodi = 0;

        for (Stagione stagione : serie.getStagioni()) {
            for (Episodio episodio : stagione.getEpisodi()) {
                totaleMinuti += episodio.getDurataEpisodio();
                totaleEpisodi++;
            }
        }

        selezioneTitoloDao.aggiungiTitoloInLista(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                serie.getIdTitolo(),
                "Visionati",
                totaleMinuti,
                totaleEpisodi
        );

        progressoDao.eliminaSerieDaProgressioSerie(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                serie.getIdTitolo())
        ;
    }
}