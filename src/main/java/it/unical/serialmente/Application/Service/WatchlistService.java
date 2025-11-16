package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.TitoloDAOPostgres;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class WatchlistService {

    // private final TMDbRequest tmdbRequest = new TMDbRequest();
    // private final TMDbHttpClient tmdbHttpClient = new TMDbHttpClient();
    // private final Mapper mapper = new Mapper();
    private final TitoloService titoloService = new TitoloService();
    private final TitoloDAOPostgres titoloDao = new TitoloDAOPostgres(DBManager.getInstance().getConnection());
    private final SelezioneTitoloDAOPostgres selezioneTitoloDao = new SelezioneTitoloDAOPostgres(DBManager.getInstance().getConnection());
    private final ProgressoSerieDAOPostgres  progressoDao = new ProgressoSerieDAOPostgres(DBManager.getInstance().getConnection());

    public void inserisciTitoloInWatchlist(Titolo titolo) throws SQLException {
        try {

            DBManager.getInstance().getConnection().setAutoCommit(false);

            boolean inserimentoConSuccesso = false;
            boolean primoInserimento=true;
            if(titoloDao.restituisciTitoloPerId(titolo.getIdTitolo()) == null) {
                primoInserimento=titoloDao.aggiungiTitolo(titolo);
            }

            if(!primoInserimento) {
                System.out.println("Esco 1");
                DBManager.getInstance().getConnection().rollback();
                return;
            }

            inserimentoConSuccesso = selezioneTitoloDao.aggiungiTitoloInLista(SessioneCorrente.getUtenteCorrente().getIdUtente(),
                    titolo.getIdTitolo(),
                    "Watchlist"
            );

            if(!inserimentoConSuccesso) {
                System.out.println("Esco 2");
                DBManager.getInstance().getConnection().rollback();

                return;
            }

            if(titolo.getTipologia().equals("SerieTv")) {

                titoloService.setDati(titolo);

                SerieTV s = (SerieTV) titolo;

                Stagione primaStagione = s.getStagioni().getFirst();
                Episodio primoEpisodio = primaStagione.getEpisodi().getFirst();

                System.out.println("inserisco:" + primaStagione.getIdStagione());
                System.out.println("inserisco:" + primoEpisodio.getIdEpisodio());

                inserimentoConSuccesso = progressoDao.creaIstanzaProgressoSerie(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        s.getIdTitolo(),
                        primoEpisodio.getIdEpisodio(),
                        primaStagione.getIdStagione(),
                        primoEpisodio.getDescrizioneEpisodio(),
                        primoEpisodio.getDurataEpisodio(),
                        1,
                        primaStagione.getNumeroStagioneProgressivo()
                );

//                String urlSerieTV = tmdbRequest.getTitolo(titolo.getIdTitolo(), "tv");
//                String infoSerieTV = tmdbHttpClient.richiesta(urlSerieTV);
//
//                String urlStagione = "/tv/" +
//                    titolo.getIdTitolo() +
//                    "/season/" +
//                    progressoDao.getNumeroProgressivoStagione(
//                            SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                            titolo.getIdTitolo()
//                    );
//
//                String infoStagione = tmdbHttpClient.richiesta(urlStagione);
//
//                ContenitoreDatiProgressoSerie c = mapper.getDatiProgressoSerie(infoSerieTV, infoStagione, 0, 0);
//                inserimentoConSuccesso = progressoDao.creaIstanzaProgressoSerie(
//                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                        titolo.getIdTitolo(),
//                        c.idEpisodio,
//                        c.idStagione,
//                        c.annoPubblicazione,
//                        c.descrizioneEpisodio,
//                        c.durataEpisodio,
//                        c.numeroProgressivoStagione,
//                        c.numeroProgressivoEpisodio
//                        );
            }

            if(!inserimentoConSuccesso) {
                System.out.println("Esco 3");
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

    public void rimuviSerieWatchlist(Titolo titolo) throws SQLException {
        DBManager.getInstance().getConnection().setAutoCommit(false);
        boolean successo= selezioneTitoloDao.eliminaTitoloInLista(SessioneCorrente.getUtenteCorrente().getIdUtente(),
                titolo.getIdTitolo(),
                "Watchlist");
        if(!successo) {
            DBManager.getInstance().getConnection().rollback();
            return;
        }
        successo= progressoDao.eliminaSerieDaProgressioSerie(SessioneCorrente.getUtenteCorrente().getIdUtente(),titolo.getIdTitolo());
        if (!successo) {
            DBManager.getInstance().getConnection().rollback();
            return;
        }
        DBManager.getInstance().getConnection().commit();
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

    public void rendiStagioneVisionata(Titolo titolo) {

        SerieTV s = (SerieTV) titolo;
        Stagione stagioneSuccessiva = null;

        for (int i = 0; i < s.getStagioni().size() - 1; i++) {
            Stagione stagione = s.getStagioni().get(i);

            if (Objects.equals(stagione.getIdStagione(), progressoDao.getIdStagioneCorrente(SessioneCorrente.getUtenteCorrente().getIdUtente(), s.getIdTitolo()))) {
                stagioneSuccessiva = s.getStagioni().get(i + 1);
            }
        }

        if (stagioneSuccessiva == null) {
            rendiTitoloVisionato(titolo);
            return;
        }

        progressoDao.cambiaStagioneCorrente(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                s.getIdTitolo(),
                stagioneSuccessiva.getIdStagione(),
                stagioneSuccessiva.getNumeroStagioneProgressivo()
        );
    }

        // String url = tmdbRequest.getTitolo(titolo.getIdTitolo(), "tv");
        // String rispostaIdProssimaStagione = tmdbHttpClient.richiesta(url);
        // Integer idProssimaStagione = mapper.parseIdProssimaStagione(
        //        rispostaIdProssimaStagione,
        //        progressoDao.getIdStagioneCorrente(
        //                SessioneCorrente.getUtenteCorrente().getIdUtente(),
        //                titolo.getIdTitolo())
        //);

        //if(idProssimaStagione == null) {
        //    rendiTitoloVisionato(titolo);
        //    return;
        //}
//        String infoSerieTV = tmdbHttpClient.richiesta(url);
//        String urlStagione = "/tv/" +
//            titolo.getIdTitolo() +
//            "/season/" +
//            progressoDao.getNumeroProgressivoStagione(
//                    SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                    titolo.getIdTitolo()
//            );
//        String infoStagione = tmdbHttpClient.richiesta(urlStagione);
//        ContenitoreDatiProgressoSerie c = mapper.getDatiProgressoSerie(
//                infoSerieTV, infoStagione,
//                progressoDao.getNumeroProgressivoStagione(SessioneCorrente.getUtenteCorrente().getIdUtente(), titolo.getIdTitolo()),
//                0
//        );
//
//        progressoDao.cambiaStagioneCorrente(
//                SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                titolo.getIdTitolo(),
//                c.annoPubblicazione,
//                c.idStagione,
//                c.numeroProgressivoStagione
//        );

    public void rendiEpisodioVisionato(Titolo titolo) throws Exception {

        titoloService.setDati(titolo);

        SerieTV s = (SerieTV) titolo;

        Episodio prossimoEpisodio = null;
        int numProgressivoProssimoEpisodio = 0;

        Integer idStagioneCorrente = progressoDao.getIdStagioneCorrente(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                s.getIdTitolo()
        );

        for (int i = 0; i < s.getStagioni().size() - 1; i++) {
            Stagione stagione = s.getStagioni().get(i);

            if (Objects.equals(
                    String.valueOf(stagione.getIdStagione()).trim(),
                    String.valueOf(idStagioneCorrente).trim()
            )) {

                for (int k = 0; k < stagione.getEpisodi().size() - 1; k++) {

                    Episodio e = stagione.getEpisodi().get(k);

                    Integer idEpisodioCorrente = progressoDao.getIdEpisodioCorrente(
                            SessioneCorrente.getUtenteCorrente().getIdUtente(),
                            s.getIdTitolo()
                    );

                    if(Objects.equals(
                            String.valueOf(e.getIdEpisodio()).trim(),
                            String.valueOf(idEpisodioCorrente).trim()
                    )){
                        prossimoEpisodio = stagione.getEpisodi().get(k + 1);
                        numProgressivoProssimoEpisodio = k + 2;
                    }
                }
            }
        }

        if (prossimoEpisodio == null) {
            rendiStagioneVisionata(titolo);
            return;
        }

        progressoDao.cambiaEpisodioCorrente(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                s.getIdTitolo(),
                prossimoEpisodio.getDescrizioneEpisodio(),
                prossimoEpisodio.getDurataEpisodio(),
                prossimoEpisodio.getIdEpisodio(),
                numProgressivoProssimoEpisodio
        );
    }

//        String urlProssimoEpisodio = tmdbRequest.getEpisodiDaStagione(
//            titolo.getIdTitolo(),
//            progressoDao.getNumeroProgressivoStagione(
//                    SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                    titolo.getIdTitolo()
//            )
//        );
//        String RispostaIdProssimoEpisodio = tmdbHttpClient.richiesta(urlProssimoEpisodio);
//        Integer idProssimoEpisodio = mapper.parseIdProssimoEpisodio(
//                RispostaIdProssimoEpisodio,
//                progressoDao.getIdEpisodioCorrente(
//                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                        titolo.getIdTitolo()
//                )
//        );
//
//        if(idProssimoEpisodio == null) {
//            rendiStagioneVisionata(titolo);
//            return;
//        }
//
//        String urlSerieTV = tmdbRequest.getTitolo(titolo.getIdTitolo(), "tv");
//        String infoSerieTV = tmdbHttpClient.richiesta(urlSerieTV);
//        String url = "/tv/" +
//                titolo.getIdTitolo() +
//                "/season/" +
//                progressoDao.getNumeroProgressivoStagione(
//                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                        titolo.getIdTitolo()
//                );
//        String infoStagione = tmdbHttpClient.richiesta(url);
//        ContenitoreDatiProgressoSerie c = mapper.getDatiProgressoSerie(
//                infoSerieTV,
//                infoStagione,
//                progressoDao.getNumeroProgressivoStagione(
//                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                        titolo.getIdTitolo()
//                ),
//                progressoDao.getNumeroProgressivoEpisodio(
//                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                        titolo.getIdTitolo()
//                )
//        );
//
//        progressoDao.cambiaEpisodioCorrente(
//                SessioneCorrente.getUtenteCorrente().getIdUtente(),
//                titolo.getIdTitolo(),
//                c.descrizioneEpisodio,
//                c.durataEpisodio,
//                c.idEpisodio,
//                c.numeroProgressivoEpisodio
//        );
//    }

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
        return progressoDao.getNomeEpisodio(
                SessioneCorrente.getUtenteCorrente().getIdUtente(),
                idSerie);
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
}