package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Application.Mapper.Mapper;
import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.API.TMDbHttpClient;
import it.unical.serialmente.TechnicalServices.API.TMDbRequest;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.GenereDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TitoloService {

    private final TMDbHttpClient tmdbHttpClient = new TMDbHttpClient();
    private final TMDbRequest tmdbRequest = new TMDbRequest();

    private final Mapper mapper = new Mapper();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ProgressoSerieDAOPostgres progressoSerieDao = new ProgressoSerieDAOPostgres(
            DBManager.getInstance().getConnection()
    );
    private final GenereDAOPostgres genereDao = new GenereDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    public Integer getIdGenereDaNome(String nome) {
        return genereDao.getGenereDaNome(nome);
    }

    public Integer getNumeroEpisodiStagione(Integer idSerieTV) throws Exception {
        String url = tmdbRequest.getSerieTV(idSerieTV);
        return mapper.parseNumeroEpisodiDaStagione(tmdbHttpClient.richiesta(url));
    }

    public Integer sommaEpisodiVisti(Integer idSerieTV, Integer numProgressivoStagione) throws Exception {
        String url = tmdbRequest.getSerieTV(idSerieTV);
        String risposta = tmdbHttpClient.richiesta(url);
        List<Stagione> stagioni = mapper.parseStagioni(risposta);

        for(Stagione stagione : stagioni) {
            stagione.setEpisodi(
                    getEpisodi(stagione.getIdStagione(), stagione.getNumeroStagioneProgressivo())
            );
        }

        int somma = 0;
        for(Stagione stagione : stagioni) {
            if(!stagione.getNomeStagione().equals("Specials")) {
                if(Objects.equals(stagione.getNumeroStagioneProgressivo(), numProgressivoStagione)) return somma;

                somma += stagione.getEpisodi().size();
            }
        }

        return somma;
    }

    public Integer sommaMinutiEpisodiVisti(Integer idSerieTV, Integer numProgressivoStagione, Integer idEpisodioCorrente) throws Exception {
        int sommaMinuti = 0;
        for(int i = 1; i < numProgressivoStagione; i++){
            String url = tmdbRequest.getEpisodiDaStagione(idSerieTV, i);
            String risposta = tmdbHttpClient.richiesta(url);
            List<Episodio> episodi = mapper.parseEpisodiDiUnaStagione(risposta);

            for(Episodio episodio : episodi) {
                sommaMinuti += episodio.getDurataEpisodio();
            }
        }

        String url = tmdbRequest.getEpisodiDaStagione(idSerieTV, numProgressivoStagione);
        String risposta = tmdbHttpClient.richiesta(url);
        List<Episodio> episodi = mapper.parseEpisodiDiUnaStagione(risposta);

        for(Episodio episodio : episodi) {
            if(Objects.equals(episodio.getIdEpisodio(), idEpisodioCorrente)) return sommaMinuti;

            sommaMinuti += episodio.getDurataEpisodio();
        }

        return sommaMinuti;
    }

    public CompletableFuture<List<Titolo>> loadPage(Integer idGenere, String tipologia, Integer pagina) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = tmdbRequest.getTitoliPerGenere(idGenere, tipologia, "&page=" + pagina);
                String risposta = tmdbHttpClient.richiesta(url);
                Pair<JSONArray, Integer> p = mapper.parseRisultatoPair(risposta, "results");

                JSONArray jsonArray = p.getKey();
                List<Titolo> titoli = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    switch (tipologia) {
                        case "movie":
                            Film f = mapper.parseFilmDaJSON(jsonObject);
                            f.setDurataMinuti(mapper.parseDurataFilm(
                                    tmdbHttpClient.richiesta(
                                            tmdbRequest.getDurataFilm(((Titolo) f).getIdTitolo())
                                    ))
                            );
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

    public CompletableFuture<List<Titolo>> getTitoliPerGenerePaginaSingola(Integer idGenere, String tipologia, Integer pagina) {
        return loadPage(idGenere, tipologia, pagina);
    }

    public List<Stagione> getStagioni(Integer idSerieTV) throws Exception {
        String url = tmdbRequest.getSerieTV(idSerieTV);
        String risposta = tmdbHttpClient.richiesta(url);
        List<Stagione> stagioni = mapper.parseStagioni(risposta);

        for(Stagione stagione : stagioni){
            stagione.setEpisodi(
                    getEpisodi(idSerieTV, stagione.getNumeroStagioneProgressivo())
            );
        }
        return stagioni;
    }

    public List<Episodio> getEpisodi(Integer idSerieTV, Integer numProgressivoStagione) throws Exception {
        String url = tmdbRequest.getEpisodiDaStagione(idSerieTV, numProgressivoStagione);
        String risposta = tmdbHttpClient.richiesta(url);
        return mapper.parseEpisodiDiUnaStagione(risposta);
    }

     public List<Titolo> getTitoliConsigliati(List<Genere> generi, List<Piattaforma> piattaforme, String tipologiaTitolo, Integer pagina) throws Exception {
         String url = tmdbRequest.cercaTitoliPerCriteri(tipologiaTitolo, generi, null, piattaforme, "&page=" + pagina);
         String risposta = tmdbHttpClient.richiesta(url);
         return mapper.parseTitoli(risposta, tipologiaTitolo);
     }

    public List<Titolo> cercaTitolo(String nomeTitolo, String tipologia, List<Genere> generi, Integer annoPubblicazione, Integer pagina) throws Exception {
        String url;
        if(nomeTitolo != null) url = tmdbRequest.cercaTitoloPerNome(nomeTitolo, tipologia);
        else url = tmdbRequest.cercaTitoliPerCriteri(tipologia, generi, annoPubblicazione, null, "&page=" + pagina);
        String risposta = tmdbHttpClient.richiesta(url);
        return mapper.parseTitoli(risposta, tipologia);
    }

    public List<Titolo> getTitoliPiuVisti(String tipologiaTitolo, Integer pagina) throws Exception {
        String url = tmdbRequest.getTitoliConSort(tipologiaTitolo, "popularity.desc", "&page=" + pagina);
        String risposta = tmdbHttpClient.richiesta(url);
        return mapper.parseTitoli(risposta, tipologiaTitolo);
    }

    public List<Titolo> getTitoliNovita(String tipologia) throws Exception {

        if(!tipologia.equals("movie") && !tipologia.equals("tv")) {
            throw new IllegalArgumentException("Tipologia non valida: " + tipologia);
        }

        String url = switch (tipologia) {
            case "movie" -> tmdbRequest.getTitoliConSort(tipologia, "primary_release_date.desc", "");
            case "tv" -> tmdbRequest.getTitoliConSort(tipologia, "first_air_date.desc", "");
            default -> "";
        };

        return mapper.parseTitoli(tmdbHttpClient.richiesta(url), tipologia);
    }

    public void popolaListaSerieTV(List<Titolo> titoli) throws Exception {

        for (Titolo titolo : titoli) {

            if (titolo.getTipologia().equals("SerieTv")) {

                List<Stagione> stagioni = getStagioni(titolo.getIdTitolo());
                SerieTV s = (SerieTV) titolo;

                for (Stagione stagione : stagioni) {
                    List<Episodio> episodi = getEpisodi(titolo.getIdTitolo(), stagione.getNumeroStagioneProgressivo());
                    stagione.setEpisodi(episodi);
                }
                s.setStagioni(stagioni);
            }
        }
    }

    public void rendiEpisodiVistiSerieTV(List<Titolo> titoli) throws Exception {

        for (Titolo titolo : titoli) {
            if (titolo.getTipologia().equals("SerieTv")) {
                SerieTV s = (SerieTV) titolo;

                ContenitoreDatiProgressoSerie c = progressoSerieDao.getDatiCorrenti(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        s
                );

                for (int k = 0; k < s.getStagioni().size(); k++) {
                    Stagione stagione = s.getStagioni().get(k);

                    for (int j = 0; j < stagione.getEpisodi().size(); j++) {
                        if (!Objects.equals(stagione.getEpisodi().get(j).getIdEpisodio(), c.idEpisodio)) {
                            stagione.getEpisodi().get(j).setVisualizzato(true);
                        } else return;
                    }

                    stagione.setCompletata(true);
                }
            }
        }
    }
}