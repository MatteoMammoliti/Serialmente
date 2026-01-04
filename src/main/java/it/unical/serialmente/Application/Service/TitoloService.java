package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Application.Mapper.Mapper;
import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.API.TMDbHttpClient;
import it.unical.serialmente.TechnicalServices.API.TMDbRequest;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.GenereDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.GenereDAOPostgres;
import it.unical.serialmente.TechnicalServices.Utility.ThreadPool;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class TitoloService {

    private final TMDbHttpClient tmdbHttpClient = new TMDbHttpClient();
    private final TMDbRequest tmdbRequest = new TMDbRequest();
    private final Mapper mapper = new Mapper();

    private final ExecutorService executor = ThreadPool.get();

    private final GenereDAO genereDao = new GenereDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    /**
     * Dato un nome di un genere, restituisce il suo id.
     * @param nome nome del genere.
     * @return id del genere.
     */
    public Integer getIdGenereDaNome(String nome) {
        return genereDao.getGenereDaNome(nome);
    }

    /**
     * Dato un genere, una tipologia, e il numero della pagine del risultato del Json restituisce una lista di titoli
     * @param idGenere id del genere.
     * @param tipologia Film o SerieTv
     * @return Lista di titoli.
     */
    public CompletableFuture<List<Titolo>> getTitoliPerGenerePaginaSingola(Integer idGenere, String tipologia, Integer pagina) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = tmdbRequest.getTitoliPerGenere(idGenere, tipologia, "&page=" + pagina);
                String risposta = tmdbHttpClient.richiesta(url);

                JSONObject obj = new JSONObject(risposta);
                JSONArray jsonArray =obj.getJSONArray("results");
                List<Titolo> titoli = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    switch (tipologia) {
                        case "movie":
                            Film f = mapper. parseFilmDaJSON(jsonObject);
                            titoli.add(f);
                            break;

                        case "tv":
                            titoli.add(mapper.parseSerieTVDaJSON(jsonObject));
                            break;
                    }
                }
                return titoli;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    /**
     * Dati dei generi, delle piattaforme e la tipologia del titolo, restituisce titoli pertinenti.
     */
     public List<Titolo> getTitoliConsigliati(List<Genere> generi,
                                              List<Piattaforma> piattaforme,
                                              String tipologiaTitolo,
                                              Integer pagina) throws Exception {

         String url = tmdbRequest.cercaTitoliPerCriteri(
                 tipologiaTitolo,
                 generi,
                 null,
                 piattaforme,
                 "page=" + pagina
         );

         String risposta = tmdbHttpClient.richiesta(url);
         return mapper.parseTitoli(risposta, tipologiaTitolo);
     }

    /**
     * Ricerca un titolo che rispetti le caratteristiche date.
     * @param nomeTitolo Nome del titolo.
     * @param tipologia Film o SerieTv
     * @param generi Generi a cui il titolo deve appartenere.
     */
    public List<Titolo> cercaTitolo(String nomeTitolo,
                                    String tipologia,
                                    List<Genere> generi,
                                    Integer annoPubblicazione,
                                    Integer pagina) throws Exception {

        if(nomeTitolo != null) {
            String rispostaRicercaPerTitolo = tmdbHttpClient.richiesta(
                    tmdbRequest.cercaTitoloPerNome(nomeTitolo, tipologia)
            );

            return mapper.parseTitoli(rispostaRicercaPerTitolo, tipologia);
        }

        String rispostaRicercaPerCriteriFilm = tmdbHttpClient.richiesta(
                tmdbRequest.cercaTitoliPerCriteri(
                        "movie",
                        generi,
                        annoPubblicazione,
                        null,
                        "page=" + pagina)
        );

        List<Titolo> film = mapper.parseTitoli(rispostaRicercaPerCriteriFilm, "movie");

        if(Objects.equals(tipologia, "movie")) return film;

        String rispostaRicercaPerCriteriSerieTV = tmdbHttpClient.richiesta(
                tmdbRequest.cercaTitoliPerCriteri(
                        "tv",
                        generi,
                        annoPubblicazione,
                        null,
                        "page=" + pagina)
        );

        List<Titolo> serieTV = mapper.parseTitoli(rispostaRicercaPerCriteriSerieTV, "tv");

        if(Objects.equals(tipologia, "tv")) return serieTV;

        film.addAll(serieTV);
        return film;
    }

    /**
     *Ritorna una lista di titoli con una popolarità maggiore presente nel catalogo esterno.
     */
    public List<Titolo> getTitoliPiuVisti(String tipologiaTitolo, Integer pagina) throws Exception {
        String url = tmdbRequest.getTitoliConSort(tipologiaTitolo, "popularity.desc", "&page=" + pagina);
        String risposta = tmdbHttpClient.richiesta(url);
        return mapper.parseTitoli(risposta, tipologiaTitolo);
    }

    /**
     *Ritorna una lista di titoli di recente uscitapresente nel catalogo esterno.
     */
    public List<Titolo> getTitoliNovita(String tipologia) throws Exception {

        String url = switch (tipologia) {
            case "movie" -> tmdbRequest.getTitoliConSort(tipologia, "primary_release_date.desc", "");
            case "tv" -> tmdbRequest.getTitoliConSort(tipologia, "first_air_date.desc", "");
            default -> "";
        };

        return mapper.parseTitoli(tmdbHttpClient.richiesta(url), tipologia);
    }

    /**
     * Serve a riempire l'oggetto titolo con tutte le sue informazioni
     * @param titolo Titolo da "riemprie".
     */
    public Titolo setDati(Titolo titolo) {

        titolo.getGeneriPresenti().clear();
        titolo.getPiattaforme().clear();

        String tipologiaTitolo = titolo.getTipologia().equals("Film") ? "movie" : "tv";

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        if (titolo.getTipologia().equals("Film")) {
            Film f = (Film) titolo;

            CompletableFuture<Void> durata = CompletableFuture.runAsync(() -> {
                try {
                    int durataFilm = mapper.parseDurataFilm(
                            tmdbHttpClient.richiesta(
                                    tmdbRequest.getDurataFilm(titolo.getIdTitolo())
                            )
                    );
                    f.setDurataMinuti(durataFilm);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            futures.add(durata);

        } else if(titolo.getTipologia().equals("SerieTv")) {
            SerieTV s = (SerieTV) titolo;

            CompletableFuture<Void> popolamento = CompletableFuture.runAsync(() -> {
                try {
                    popolaListaSerieTV(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            futures.add(popolamento);
        }

        CompletableFuture<Void> generi = CompletableFuture.runAsync(() -> {
            try {
                List<Genere> generiTitolo = mapper.parseGeneri(
                        tmdbHttpClient.richiesta(
                                tmdbRequest.getTitolo(titolo.getIdTitolo(), tipologiaTitolo)
                        )
                );
                generiTitolo.forEach(titolo::aggiungiGenere);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        futures.add(generi);

        CompletableFuture<Void> piattaforme = CompletableFuture.runAsync(() -> {
            try {
                List<Piattaforma> piattaformeTitolo = mapper.parsePiattaforme(
                        tmdbHttpClient.richiesta(
                                tmdbRequest.getPiattaforme(titolo.getIdTitolo(), tipologiaTitolo)
                        ),
                        tipologiaTitolo.equals("movie")
                );
                piattaformeTitolo.forEach(titolo::aggiungiPiattaforme);
            } catch (Exception e)  {
                throw new RuntimeException(e);
            }
        });
        futures.add(piattaforme);

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return titolo;
    }

    /**
     * Ritorna una lista di titoli del catalogo esterno casuali
     */
    public List<Titolo> getTitoliCasuali() throws Exception {

        String url = tmdbRequest.getTitoliCasuali(
                "movie",
                1
        );

        String risposta = tmdbHttpClient.richiesta(url);
        List<Titolo> film = mapper.parseTitoli(risposta, "movie");

        url = tmdbRequest.getTitoliCasuali(
                "tv",
                1
        );

        risposta = tmdbHttpClient.richiesta(url);
        List<Titolo> tv = mapper.parseTitoli(risposta, "tv");

        film.addAll(tv);
        return film;
    }

    /**
     * Ritorna la lista di stagioni appartenenti alla serie Tv
     */
    private List<Stagione> getStagioni(Integer idSerieTV) throws Exception {
        String url = tmdbRequest.getTitolo(idSerieTV, "tv");
        String risposta = tmdbHttpClient.richiesta(url);
        List<Stagione> stagioni = mapper.parseStagioni(risposta);

        for(Stagione stagione : stagioni){
            stagione.setEpisodi(
                    getEpisodi(idSerieTV, stagione.getNumeroStagioneProgressivo())
            );
        }
        return stagioni;
    }

    /**
     * Ritorna una lista di Episodi appartenente alla stagione corrente di una data serieTv.
     */
    private List<Episodio> getEpisodi(Integer idSerieTV, Integer numProgressivoStagione) throws Exception {
        String url = tmdbRequest.getEpisodiDaStagione(numProgressivoStagione, idSerieTV);
        String risposta = tmdbHttpClient.richiesta(url);
        return mapper.parseEpisodiDiUnaStagione(risposta);
    }

    /**
     * Riempie una serie tv con i propri episodi e le proprie stagioni.
     */
    private void popolaListaSerieTV(SerieTV s) throws Exception {
        List<Stagione> stagioni = getStagioni(s.getIdTitolo());

        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        for (Stagione stagione : stagioni) {

            CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                try {
                    List<Episodio> episodi = getEpisodi(s.getIdTitolo(), stagione.getNumeroStagioneProgressivo());
                    stagione.setEpisodi(episodi);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executor);
            tasks.add(task);
        }

        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

        s.setStagioni(stagioni);
    }
}