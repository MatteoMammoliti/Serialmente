package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Application.Mapper.Mapper;
import it.unical.serialmente.Domain.model.ContenitoreDatiProgressoSerie;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.TechnicalServices.API.TMDbHttpClient;
import it.unical.serialmente.TechnicalServices.API.TMDbRequest;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.TitoloDAOPostgres;

import java.sql.SQLException;
import java.util.List;

public class WatchlistService {

    private final TMDbRequest tmdbRequest = new TMDbRequest();
    private final TMDbHttpClient tmdbHttpClient = new TMDbHttpClient();
    private final Mapper mapper = new Mapper();
    private final TitoloDAOPostgres titoloDao = new TitoloDAOPostgres(DBManager.getInstance().getConnection());
    private final SelezioneTitoloDAOPostgres selezioneTitoloDao = new SelezioneTitoloDAOPostgres(DBManager.getInstance().getConnection());
    private final ProgressoSerieDAOPostgres  progressoDao = new ProgressoSerieDAOPostgres(DBManager.getInstance().getConnection());
    private final TitoloService titoloService = new TitoloService();

    public void inserisciTitoloInWatchlist(Titolo titolo) throws SQLException {
        try {
            DBManager.getInstance().getConnection().setAutoCommit(false);
            boolean inserimentoConSuccesso = false;
            if(titoloDao.restituisciTitoloPerId(titolo.getIdTitolo()) == null) {
                inserimentoConSuccesso = titoloDao.aggiungiTitolo(titolo);
            }

            if(!inserimentoConSuccesso) {
                DBManager.getInstance().getConnection().rollback();
                return;
            }

            inserimentoConSuccesso = selezioneTitoloDao.aggiungiTitoloInLista(SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo(),
                    "Watchlist"
            );

            if(!inserimentoConSuccesso) {
                DBManager.getInstance().getConnection().rollback();
                return;
            }

            if(titolo.getTipologia().equals("SerieTv")) {
                String urlSerieTV = tmdbRequest.getSerieTV(titolo.getIdTitolo());
                String infoSerieTV = tmdbHttpClient.richiesta(urlSerieTV);

                String urlStagione = "/tv/" +
                    titolo.getIdTitolo() +
                    "/season/" +
                    progressoDao.getNumeroProgressivoStagione(
                            SessioneCorrente.getUtenteCorrente().getIdUtente(),
                            titolo.getIdTitolo()
                    );

                String infoStagione = tmdbHttpClient.richiesta(urlStagione);

                ContenitoreDatiProgressoSerie c = mapper.getDatiProgressoSerie(infoSerieTV, infoStagione, 0, 0);
                inserimentoConSuccesso = progressoDao.creaIstanzaProgressoSerie(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo(),
                        c.idEpisodio,
                        c.idStagione,
                        c.annoPubblicazione,
                        c.descrizioneEpisodio,
                        c.durataEpisodio,
                        c.numeroProgressivoStagione,
                        c.numeroProgressivoEpisodio
                        );
            }

            if(!inserimentoConSuccesso) {
                DBManager.getInstance().getConnection().rollback();
                return;
            }

            DBManager.getInstance().getConnection().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBManager.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void rimuoviTitoloDallaWatchlist(Titolo titolo) throws SQLException {

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

    public void rendiTitoloVisionato(Titolo titolo) {

        selezioneTitoloDao.aggiungiTitoloInLista(SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                "Visionati"
        );

        if(titolo.getTipologia().equals("SerieTv"))
            progressoDao.eliminaSerieDaProgressioSerie(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo()
        );
    }

    public void rendiStagioneVisionata(Titolo titolo) throws Exception {
        String url = tmdbRequest.getSerieTV(titolo.getIdTitolo());
        String rispostaIdProssimaStagione = tmdbHttpClient.richiesta(url);
        Integer idProssimaStagione = mapper.parseIdProssimaStagione(
                rispostaIdProssimaStagione,
                progressoDao.getIdStagioneCorrente(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo())
        );

        if(idProssimaStagione == null) {
            rendiTitoloVisionato(titolo);
            return;
        }

        String infoSerieTV = tmdbHttpClient.richiesta(url);
        String urlStagione = "/tv/" +
            titolo.getIdTitolo() +
            "/season/" +
            progressoDao.getNumeroProgressivoStagione(
                    SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo()
            );
        String infoStagione = tmdbHttpClient.richiesta(urlStagione);
        ContenitoreDatiProgressoSerie c = mapper.getDatiProgressoSerie(
                infoSerieTV, infoStagione,
                progressoDao.getNumeroProgressivoStagione(SessioneCorrente.getUtenteCorrente().getIdUtente(), titolo.getIdTitolo()),
                0
        );

        progressoDao.cambiaStagioneCorrente(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                c.annoPubblicazione,
                c.idStagione,
                c.numeroProgressivoStagione
        );
    }

    public void rendiEpisodioVisionato(Titolo titolo) throws Exception {
        String urlProssimoEpisodio = tmdbRequest.getEpisodiDaStagione(
            titolo.getIdTitolo(),
            progressoDao.getNumeroProgressivoStagione(
                    SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo()
            )
        );
        String RispostaIdProssimoEpisodio = tmdbHttpClient.richiesta(urlProssimoEpisodio);
        Integer idProssimoEpisodio = mapper.parseIdProssimoEpisodio(
                RispostaIdProssimoEpisodio,
                progressoDao.getIdEpisodioCorrente(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo()
                )
        );

        if(idProssimoEpisodio == null) {
            rendiStagioneVisionata(titolo);
            return;
        }

        String urlSerieTV = tmdbRequest.getSerieTV(titolo.getIdTitolo());
        String infoSerieTV = tmdbHttpClient.richiesta(urlSerieTV);
        String url = "/tv/" +
                titolo.getIdTitolo() +
                "/season/" +
                progressoDao.getNumeroProgressivoStagione(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo()
                );
        String infoStagione = tmdbHttpClient.richiesta(url);
        ContenitoreDatiProgressoSerie c = mapper.getDatiProgressoSerie(
                infoSerieTV,
                infoStagione,
                progressoDao.getNumeroProgressivoStagione(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo()
                ),
                progressoDao.getNumeroProgressivoEpisodio(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        titolo.getIdTitolo()
                )
        );

        progressoDao.cambiaEpisodioCorrente(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                c.descrizioneEpisodio,
                c.durataEpisodio,
                c.idEpisodio,
                c.numeroProgressivoEpisodio
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
    public String getNomeEpisodio(Integer idSerie){
        return progressoDao.getNomeEpisodio(SessioneCorrente.getUtenteCorrente().getIdUtente(),idSerie);
    }
    public Integer getNumeroStagione(Integer idSerie){
        return progressoDao.getNumeroProgressivoStagione(SessioneCorrente.getUtenteCorrente().getIdUtente(),idSerie);
    }
    public Integer getNumeroEpisodio(Integer idSerie){
        return  progressoDao.getNumeroProgressivoEpisodio(SessioneCorrente.getUtenteCorrente().getIdUtente(),idSerie);
    }

    public boolean controlloPresenzaTitoloInListe(Integer idTitolo){
        return selezioneTitoloDao.controlloTitoloInListeUtente(SessioneCorrente.getUtenteCorrente().getIdUtente(),idTitolo);
    }
    public boolean controlloPresenzaSerieTvInListe(Integer idSerie){
        return selezioneTitoloDao.controlloTitoloInListeUtente(SessioneCorrente.getUtenteCorrente().getIdUtente(),idSerie)&&
                progressoDao.controlloSerieTvInCorso(SessioneCorrente.getUtenteCorrente().getIdUtente(),idSerie);
    }
}