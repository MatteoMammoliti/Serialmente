package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.GenereDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.PiattaformaDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.PreferisceGenereDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.PreferiscePiattaformaDAOPostgres;

import java.util.List;

public class PreferenzeService {

    private final PreferiscePiattaformaDAOPostgres preferiscePiattaformaDao = new PreferiscePiattaformaDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final PreferisceGenereDAOPostgres preferisceGenereDao = new PreferisceGenereDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final GenereDAOPostgres genereDao = new GenereDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final PiattaformaDAOPostgres  piattaformaDao = new PiattaformaDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    /**
     * Funzione che aggiorna (aggiuge o rimuove) le piattaforme preferite dell'utente
     * @param piattaforme
     * @param tipologiaAggiornamento accetta: AGGIUNTA o RIMOZIONE
     */
    public void aggiornaPreferenzePiattaforme(List<Piattaforma> piattaforme, String tipologiaAggiornamento) {
        Integer idUtenteCorrente = SessioneCorrente.getUtenteCorrente().getIdUtente();
        switch (tipologiaAggiornamento) {
            case "AGGIUNTA":
                for(Piattaforma i : piattaforme) {
                    preferiscePiattaformaDao.aggiungiPiattaformaPreferitaUtente(
                            idUtenteCorrente,
                            i.getIdPiattaforma()
                    );
                }
                return;
            case "RIMOZIONE":
                for(Piattaforma i : piattaforme) {
                    preferiscePiattaformaDao.rimuoviPiattaformaPreferitaUtente(
                            idUtenteCorrente,
                            i.getIdPiattaforma()
                    );
                }
        }
    }

    /**
     * Funzione che aggiorna (aggiuge o rimuove) i generi preferiti dell'utente
     * @param generi
     * @param tipologiaAggiornamento accetta: AGGIUNTA o RIMOZIONE
     */
    public void aggiornaPreferenzeGenere(List<Genere> generi, String tipologiaAggiornamento) {
        Integer idUtenteCorrente = SessioneCorrente.getUtenteCorrente().getIdUtente();
        switch (tipologiaAggiornamento) {
            case "AGGIUNTA":
                for(Genere i : generi) {
                    preferisceGenereDao.aggiungiGenerePreferitoUtente(
                            idUtenteCorrente,
                            i.getIdGenere()
                    );
                }
                return;
            case "RIMOZIONE":
                for(Genere i : generi) {
                    preferisceGenereDao.rimuoviGenerePreferitoUtente(
                            idUtenteCorrente,
                            i.getIdGenere()
                    );
                }
        }
    }

    public List<Piattaforma> visualizzaPreferenzePiattaformeUtente() {
        return preferiscePiattaformaDao.getPiattaformePreferiteUtente(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

    public List<Genere> visualizzaPreferenzeGenereUtente() {
        return preferisceGenereDao.getGeneriPreferitiUtente(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

    public List<Genere> getGeneriPerSerieTV() {
        List<Genere> g1 = genereDao.restituisciGeneriPresentiNelDB("tv");
        List<Genere> g2 =  genereDao.restituisciGeneriPresentiNelDB("both");
        g1.addAll(g2);
        return g1;
    };

    public List<Genere> getGeneriPerFilm() {
        List<Genere> g1 = genereDao.restituisciGeneriPresentiNelDB("movie");
        List<Genere> g2 =  genereDao.restituisciGeneriPresentiNelDB("both");
        g1.addAll(g2);
        return g1;
    };

    public List<Piattaforma> getPiattaformeDisponibili() {
        return piattaformaDao.getListaPiattaforma();
    }
}