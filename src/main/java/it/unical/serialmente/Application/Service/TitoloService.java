package it.unical.serialmente.Application.Service;

import it.unical.serialmente.Application.Mapper.Mapper;
import it.unical.serialmente.TechnicalServices.API.TMDbAPI;
import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TitoloService {

    private final TMDbAPI tmdb = new TMDbAPI();
    private final Mapper mapper = new Mapper();
    private final ProgressoSerieDAOPostgres progressoSerieDao = new ProgressoSerieDAOPostgres(DBManager.getInstance().getConnection());

    public Integer getNumeroEpisodiStagione(Integer idSerieTV) throws Exception {
        String risposta = tmdb.getSerieTV(idSerieTV);
        JSONObject obj = new JSONObject(risposta);
        return obj.optInt("number_of_episodes");
    }

    public Integer sommaEpisodiVisti(Integer idSerieTV, Integer numProgressivoStagione) throws Exception {
        String risposta = tmdb.getSerieTV(idSerieTV);
        JSONArray jsonArray = mapper.parseRisultato(risposta, "seasons");

        int somma = 0;
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject season = jsonArray.getJSONObject(i);
            if(!season.optString("name").equals("Specials")){
                if(season.optInt("season_number") == numProgressivoStagione) return somma;

                somma += season.optInt("episode_count");
            }
        }
        return somma;
    }

    public Integer sommaMinutiEpisodiVisti(Integer idSerieTV, Integer numProgressivoStagione, Integer numProgressivoEpisodio) throws Exception {
        int sommaMinuti = 0;
        for(int i = 1; i < numProgressivoStagione; i++){
            String risposta = tmdb.getEpisodiDaStagione(idSerieTV, i);
            JSONArray jsonArray = mapper.parseRisultato(risposta, "episodes");

            for(int k = 0; k < jsonArray.length(); k++){
                JSONObject episodio = jsonArray.getJSONObject(k);
                    sommaMinuti += episodio.optInt("runtime");
            }
        }

        String risposta = tmdb.getEpisodiDaStagione(idSerieTV, numProgressivoStagione);
        JSONArray jsonArray = mapper.parseRisultato(risposta, "episodes");

        for(int k = 0; k < jsonArray.length(); k++){
            JSONObject episodio = jsonArray.getJSONObject(k);
            if(episodio.optInt("episode_number") == numProgressivoEpisodio) return sommaMinuti;

            sommaMinuti += episodio.optInt("runtime");
        }
        return sommaMinuti;
    }

    public List<Titolo> getTitoliPerGenere(Genere g, String tipologia) throws Exception {
        String risposta = tmdb.getTitoliPerGenere(g, tipologia);
        JSONArray jsonArray = mapper.parseRisultato(risposta, "results");

        List<Titolo> titoliPerGenere = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            switch (tipologia) {
                case "movie":
                    titoliPerGenere.add(mapper.parseFilmDaJSON(jsonObject));
                    break;

                case "tv":
                    titoliPerGenere.add(mapper.parseSerieTVDaJSON(jsonObject));
                    break;
            }
        }
        return titoliPerGenere;
    }

    /**
     * Funzione che restituisce una lista di oggetti stagione per una data serie
     * @param idSerieTV
     * @return List<Stagione> stagioni
     * @throws Exception
     */
    public List<Stagione> getStagioni(Integer idSerieTV) throws Exception {
        String risposta = tmdb.getSerieTV(idSerieTV);
        JSONArray stagioniArray = mapper.parseRisultato(risposta, "seasons");

        List<Stagione> stagioni = new ArrayList<>();
        for(int i = 0; i < stagioniArray.length(); i++){
            Stagione s = mapper.parseStagioneDaJSON(stagioniArray.getJSONObject(i));
            s.setEpisodi(getEpisodi(idSerieTV, s.getNumeroStagioneProgressivo()));
            stagioni.add(s);
        }
        return stagioni;
    }

    /**
     * Funzione che restituisce una lista di oggetti episodi per una data serie tv e per una data stagione
     * @param idSerieTV
     * @param numeroStagione
     * @return List<Episodio> episodi
     * @throws Exception
     */
    public List<Episodio> getEpisodi(Integer idSerieTV, Integer numProgressivoStagione) throws Exception {
        JSONArray episodiArray = mapper.parseEpisodiDiUnaStagione(idSerieTV, numProgressivoStagione);
        return mapper.parseEpisodiDiUnaStagioneDaJSONArray(episodiArray);
    }

    /**
     * Funzione che restituisce un massimo di tot di titoli tra quelli consigliati
     * @param generi
     * @param piattaforme
     * @param tipologiaTitolo
     * @return
     * @throws Exception
     */
     public List<Titolo> getTitoliConsigliati(List<Genere> generi, List<Piattaforma> piattaforme, String tipologiaTitolo) throws Exception {
         String risposta = tmdb.getTitoliConsigliati(generi, piattaforme, tipologiaTitolo);
         List<Titolo> titoli = new ArrayList<>();
         estraiTitoli(risposta, titoli, tipologiaTitolo);
         return titoli;
     }

    /**
     * Funzione che restituisce un array di oggetti Titolo coerenti con la ricera
     * @param nomeTitolo
     * @param tipologia
     * @param generi
     * @param annoPubblicazione
     * @return List<Titolo> titoli
     * @throws Exception
     */
    public List<Titolo> cercaTitolo(String nomeTitolo, String tipologia, List<Genere> generi, Integer annoPubblicazione) throws Exception {
        String risposta = tmdb.cercaTitolo(nomeTitolo, tipologia, generi, annoPubblicazione);
        JSONArray array = mapper.parseRisultato(risposta, "results");

        List<Titolo> titoli = new ArrayList<Titolo>();

        for(int i = 0; i < array.length(); i++){
            Titolo t = mapper.parseTitoloDaJSON(array.getJSONObject(i));
            titoli.add(t);
        }
        return titoli;
    }

    /**
     * Funzione che restituisce un massimo di 10 titoli (5 serie tv e 5 film) tra quelli più visti
     * @param tipologiaTitolo
     * @return List<Titolo> di titoli più visti
     */
    public List<Titolo> getTitoliPiuVisti(String tipologiaTitolo) throws Exception {
        String risposta = tmdb.getTitoliConSort(tipologiaTitolo, "popularity.desc");

        List<Titolo> titoli = new ArrayList<>();
        estraiTitoli(risposta, titoli, tipologiaTitolo);
        return titoli;
    }

    /**
     * Funzione che restituisce un elenco di titoli tra quelli usciti di recente
     * @param tipologia
     * @return
     * @throws Exception
     */
    public List<Titolo> getTitoliNovita(String tipologia) throws Exception {

        if(!tipologia.equals("movie") && !tipologia.equals("tv")) {
            throw new IllegalArgumentException("Tipologia non valida: " + tipologia);
        }

        String risposta = switch (tipologia) {
            case "movie" -> tmdb.getTitoliConSort(tipologia, "primary_release_date.desc");
            case "tv" -> tmdb.getTitoliConSort(tipologia, "first_air_date.desc");
            default -> "";
        };

        List<Titolo> titoli = new ArrayList<>();
        estraiTitoli(risposta, titoli, tipologia);
        return titoli;
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

    /**
     * Funzione che estrae dalle risposte dell'API gli oggetti film o serie tv tra quelli più visti
     * @param risposta
     * @param titoli
     * @param tipologia
     * @throws Exception
     */
    private void estraiTitoli(String risposta, List<Titolo> titoli, String tipologia) throws Exception {
        JSONArray array = mapper.parseRisultato(risposta, "results");

        int titoliAggiunti = 0;
        for(int i = 0; i < array.length(); i++){

            if(titoliAggiunti == 200){ return; }
            JSONObject object = array.getJSONObject(i);
            Titolo t = switch (tipologia) {
                case "movie" -> mapper.parseFilmDaJSON(object);
                case "tv" -> mapper.parseSerieTVDaJSON(object);
                default -> null;
            };

            titoli.add(t);
            titoliAggiunti++;
        }
    }
}