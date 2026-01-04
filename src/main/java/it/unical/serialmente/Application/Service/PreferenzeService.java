package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.*;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.*;

import java.util.*;

public class PreferenzeService {

    private final PreferiscePiattaformaDAO preferiscePiattaformaDao = new PreferiscePiattaformaDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final PreferisceGenereDAO preferisceGenereDao = new PreferisceGenereDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final GenereDAO genereDao = new GenereDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final PiattaformaDAO piattaformaDao = new PiattaformaDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    private final SelezioneTitoloDAO selezioneTitoloDao = new  SelezioneTitoloDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    /**
     * Funzione che aggiorna (aggiuge o rimuove) le piattaforme preferite dell'utente
     * @param piattaforme Lista di piattaforme da aggiungere o rimuovere
     * @param tipologiaAggiornamento accetta: AGGIUNTA o RIMOZIONE
     */
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
     * @param generi Id dei generi da aggiungere o rimuovere.
     * @param tipologiaAggiornamento accetta: AGGIUNTA o RIMOZIONE
     */
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

    /**
     * Ritorna la lista delle piattaforme preferite dall'utente.
     * @return lista di piattaforme preferita dall'utente.
     */
    public List<Piattaforma> visualizzaPreferenzePiattaformeUtente() throws Exception {
        return preferiscePiattaformaDao.getPiattaformePreferiteUtente(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

    /**
     * Ritorna la lista dei generi preferiti dall'utente.
     * @return Lista contenente i Generi preferiti dall'utente.
     */
    public List<Genere> visualizzaPreferenzeGenereUtente() {
        return preferisceGenereDao.getGeneriPreferitiUtente(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );
    }

    /**
     * Restituisce i generi disponibili per le SerieTv
     * @return Lista contenente i generi per le SerieTv
     */
    public List<Genere> getGeneriPerSerieTV() {
        return getGeneriByTipo("tv");
    }

    /**
     * Restituisce i generi disponibili per i Film.
     * @return Lista contenente i generi per i Film.
     */
    public List<Genere> getGeneriPerFilm() {
        return getGeneriByTipo("movie");
    }

    /**
     * Restituisce tutte le piattaforme disponibili.
     * @return Lista contenente tutte le piattaforme disponibili.
     */
    public List<Piattaforma> getPiattaformeDisponibili() {
        return piattaformaDao.getListaPiattaforma();
    }

    /**
     * La funzione permette di eliminare o aggiungere generi e piattaforme dalle preferenze dell'utente.
     * @param idGeneriEliminare Lista contenente gli id dei generi da eliminare dalle preferenze.
     * @param idPiattaformeEliminare Lista contenente gli id delle piattaforme da eliminare dalle preferenze.
     * @param idGenereAggiuntere Lista contenente gli id dei generi da aggiungere alle preferenze.
     * @param idPiattaformeAggiuntere Lista contenente gli id delle piattaforme  da aggiungere dalle preferenze.
     */
    public void applicaModificaPreferenze(HashSet<Integer> idGeneriEliminare,
                                          HashSet<Integer> idPiattaformeEliminare,
                                          HashSet<Integer> idGenereAggiuntere,
                                          HashSet<Integer> idPiattaformeAggiuntere ) throws Exception {

        aggiornaPreferenzeGenereId(idGenereAggiuntere,"AGGIUNTA");
        aggiornaPreferenzeGenereId(idGeneriEliminare,"RIMOZIONE");
        aggiornaPreferenzePiattaformeId(idPiattaformeAggiuntere,"AGGIUNTA");
        aggiornaPreferenzePiattaformeId(idPiattaformeEliminare,"RIMOZIONE");
    }

    /**
     * Ritorna tutti i generi dei film già visionati dall'utente.
     * @return Lista contenente tutti i generi dei film già visionati dall'utente.
     */
    public List<Genere> getGeneriStoricoFilm() {
        List<Integer> idGeneri = selezioneTitoloDao.getIdGeneriFilm(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );

        return getGeneriStorico(idGeneri);
    }

    /**
     * Ritorna tutti i generi delle serieTv già visionate dall'utente.
     * @return Lista contenente tutti i generi delle SerieTv già visionate dall'utente.
     */
    public List<Genere> getGeneriStoricosSerieTV() {
        List<Integer> idGeneri = selezioneTitoloDao.getIdGeneriSerieTV(
                SessioneCorrente.getUtenteCorrente().getIdUtente()
        );

        return getGeneriStorico(idGeneri);
    }

    /**
     * Data una lista di Interi contenenti gli id dei generi estrai i generi.
     * @param idGeneri Lista contenente gli id dei generi.
     * @return Lista contenente i generi estratti dagli id.
     */
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

    /**
     * Restituisce tutti i generi presenti nel DB senza fare distinzione tra serieTv o Film.
     * @param tipo SerieTv o Film.
     */
    private List<Genere> getGeneriByTipo(String tipo) {
        List<Genere> g1 = genereDao.restituisciGeneriPresentiNelDB(tipo);
        List<Genere> g2 = genereDao.restituisciGeneriPresentiNelDB("both");
        g1.addAll(g2);
        return g1;
    }
}