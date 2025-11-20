package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.*;

import java.util.*;

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

    private final SelezioneTitoloDAOPostgres selezioneTitoloDao = new  SelezioneTitoloDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    /**
     * Funzione che aggiorna (aggiuge o rimuove) le piattaforme preferite dell'utente
     * @param piattaforme
     * @param tipologiaAggiornamento accetta: AGGIUNTA o RIMOZIONE
     */
    public void aggiornaPreferenzePiattaforme(List<Piattaforma> piattaforme, String tipologiaAggiornamento) throws Exception {
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
    public void aggiornaPreferenzePiattaformeId(HashSet<Integer> piattaforme, String tipologiaAggiornamento) throws Exception {
        Integer idUtenteCorrente = SessioneCorrente.getUtenteCorrente().getIdUtente();
        switch (tipologiaAggiornamento) {
            case "AGGIUNTA":
                for(Integer i : piattaforme) {
                    preferiscePiattaformaDao.aggiungiPiattaformaPreferitaUtente(
                            idUtenteCorrente,
                            i
                    );
                }
                return;
            case "RIMOZIONE":
                for(Integer i : piattaforme) {
                    preferiscePiattaformaDao.rimuoviPiattaformaPreferitaUtente(
                            idUtenteCorrente,
                            i
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
    public void aggiornaPreferenzeGenereId(HashSet<Integer> generi, String tipologiaAggiornamento) {
        Integer idUtenteCorrente = SessioneCorrente.getUtenteCorrente().getIdUtente();
        switch (tipologiaAggiornamento) {
            case "AGGIUNTA":
                for(Integer i : generi) {
                    preferisceGenereDao.aggiungiGenerePreferitoUtente(
                            idUtenteCorrente,
                            i
                    );
                }
                return;
            case "RIMOZIONE":
                for(Integer i : generi) {
                    preferisceGenereDao.rimuoviGenerePreferitoUtente(
                            idUtenteCorrente,
                            i
                    );
                }
        }
    }

    public List<Piattaforma> visualizzaPreferenzePiattaformeUtente() throws Exception {
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
        return getGeneriByTipo("tv");
    }

    public List<Genere> getGeneriPerFilm() {
        return getGeneriByTipo("movie");
    }

    public List<Piattaforma> getPiattaformeDisponibili() {
        return piattaformaDao.getListaPiattaforma();
    }

    public void applicaModificaPreferenze(HashSet<Integer> idGeneriEliminare, HashSet<Integer> idPiattaformeEliminare,
                                          HashSet<Integer> idGenereAggiuntere, HashSet<Integer> idPiattaformeAggiuntere ) throws Exception {

        aggiornaPreferenzeGenereId(idGenereAggiuntere,"AGGIUNTA");
        aggiornaPreferenzeGenereId(idGeneriEliminare,"RIMOZIONE");
        aggiornaPreferenzePiattaformeId(idPiattaformeAggiuntere,"AGGIUNTA");
        aggiornaPreferenzePiattaformeId(idPiattaformeEliminare,"RIMOZIONE");
    }

    public List<Genere> getGeneriStoricoFilm() {
        List<Integer> idGeneri = selezioneTitoloDao.getIdGeneriFilm(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );

        return getGeneriStorico(idGeneri);
    }

    public List<Genere> getGeneriStoricosSerieTV() {
        List<Integer> idGeneri = selezioneTitoloDao.getIdGeneriSerieTV(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );

        return getGeneriStorico(idGeneri);
    }

    private List<Genere> getGeneriStorico(List<Integer> idGeneri) {
        List<Genere> generi = new ArrayList<>();

        Map<Integer, Integer> freq = new HashMap<>();

        for (Integer id : idGeneri) {
            freq.put(id, freq.getOrDefault(id, 0) + 1);
        }

        for (var entry : freq.entrySet()) {
            if (entry.getValue() > 3) {
                Genere g = new Genere();
                g.setIdGenere(entry.getKey());
                generi.add(g);
            }
        }
        return generi;
    }

    private List<Genere> getGeneriByTipo(String tipo) {
        List<Genere> g1 = genereDao.restituisciGeneriPresentiNelDB(tipo);
        List<Genere> g2 = genereDao.restituisciGeneriPresentiNelDB("both");
        g1.addAll(g2);
        return g1;
    }
}