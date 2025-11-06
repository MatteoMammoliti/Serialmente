package it.unical.serialmente.Application.Service;

import it.unical.serialmente.TechnicalServices.API.TMDbAPI;
import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.PreferisceGenereDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.PreferiscePiattaformaDAOPostgres;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TitoloService {

    private final TMDbAPI tmdb = new TMDbAPI();
    private final PreferisceGenereDAOPostgres preferisceGenereDao = new PreferisceGenereDAOPostgres(DBManager.getInstance().getConnection());
    private final PreferiscePiattaformaDAOPostgres preferiscePiattaformaDao = new  PreferiscePiattaformaDAOPostgres(DBManager.getInstance().getConnection());


    /**
     * Funzione che restituisce un oggetto SerieTV per un id dato
     * @param idSerieTV
     * @return SerieTV
     * @throws Exception
     */
    public SerieTV getSerieTV(Integer idSerieTV) throws Exception {
        String risposta = tmdb.getSerieTV(idSerieTV);
        JSONObject json = new JSONObject(risposta);
        SerieTV s = estraiSerieTVDaJSON(json);
        return s;
    }

    /**
     * Funzione che restituisce una lista di oggetti stagione per una data serie
     * @param idSerieTV
     * @return List<Stagione> stagioni
     * @throws Exception
     */
    public List<Stagione> getStagioni(Integer idSerieTV) throws Exception {
        String risposta = tmdb.getSerieTV(idSerieTV);
        JSONObject json = new JSONObject(risposta);

        List<Stagione> stagioni = new ArrayList<>();
        JSONArray stagioniArray = json.getJSONArray("seasons");

        for(int i = 0; i < stagioniArray.length(); i++){
            JSONObject obj = stagioniArray.getJSONObject(i);

            Stagione s = new Stagione(
                    obj.getString("name"),
                    obj.getInt("id"),
                    getEpisodi(idSerieTV, obj.getInt("season_number"))
            );
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
    public List<Episodio> getEpisodi(Integer idSerieTV, Integer numeroStagione) throws Exception {
        String risposta =  tmdb.getEpisodiDaStagione(idSerieTV, numeroStagione);
        JSONObject json = new JSONObject(risposta);

        List<Episodio> episodi = new ArrayList<>();
        JSONArray episodiArray = json.getJSONArray("episodes");

        for(int i = 0; i < episodiArray.length(); i++){
            JSONObject obj = episodiArray.getJSONObject(i);
            Episodio ep = new Episodio(
                    obj.getInt("id"),
                    obj.getInt("runtime"),
                    obj.getString("overview")
            );
            episodi.add(ep);
        }
        return episodi;
    }

    /**
     * Funzione che restituisce un massimo di 10 titoli (5 serie tv e 5 film) tra quelli consigliati
     * @param generi
     * @param piattaforme
     * @return
     * @throws Exception
     */
     public List<Titolo> getTitoliConsigliati(List<Genere> generi, List<Piattaforma> piattaforme) throws Exception {
         String rispostaFilmConsigliati = tmdb.getTitoliConsigliati(generi, piattaforme, "movie");
         String rispostaSerieTVConsigliate = tmdb.getTitoliConsigliati(generi, piattaforme, "tv");

         List<Titolo> titoli = new ArrayList<>();
         estraiPiuVistiConsigliati(rispostaFilmConsigliati, titoli, "movie");
         estraiPiuVistiConsigliati(rispostaSerieTVConsigliate, titoli, "tv");
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

        JSONObject obj = new JSONObject(risposta);
        JSONArray array = obj.getJSONArray("results");

        List<Titolo> titoli = new ArrayList<Titolo>();

        for(int i = 0; i < array.length(); i++){

            JSONObject object = array.getJSONObject(i);
            Titolo t = null;

            if(object.has("original_name")) { t = estraiSerieTVDaJSON(object); }
            if(object.has("original_title")) { t = estraiFilmDaJSON(object); }

            titoli.add(t);
        }
        return titoli;
    }

    /**
     * Funzione che restituisce un massimo di 10 titoli (5 serie tv e 5 film) tra quelli più visti
     * @return List<Titolo> di titoli più visti
     */
    public List<Titolo> getTitoliPiuVisti() throws Exception {
        String rispostaFilmPiùVisti = tmdb.getTitoliPiùVisti("movie", "popularity.desc");
        String rispostaSerieTVPiùViste = tmdb.getTitoliPiùVisti("tv", "popularity.desc");

        List<Titolo> titoli = new ArrayList<>();
        estraiPiuVistiConsigliati(rispostaFilmPiùVisti, titoli, "movie");
        estraiPiuVistiConsigliati(rispostaSerieTVPiùViste, titoli, "tv");
        return titoli;
    }

    /**
     * Funzione che estrae dalle risposte dell'API gli oggetti film o serie tv tra quelli più visti
     * @param risposta
     * @param titoli
     * @param tipologia
     * @throws Exception
     */
    private void estraiPiuVistiConsigliati(String risposta, List<Titolo> titoli, String tipologia) throws Exception {
        JSONObject obj = new JSONObject(risposta);
        JSONArray array = obj.getJSONArray("results");

        Integer titoliAggiunti = 0;
        for(int i = 0; i < array.length(); i++){

            if(titoliAggiunti == 5){ return; }
            JSONObject object = array.getJSONObject(i);
            Titolo t = null;

            switch (tipologia) {
                case "movie":
                    t = estraiFilmDaJSON(object);
                    break;
                case "tv":
                    t = estraiSerieTVDaJSON(object);
                    break;
            }
            titoli.add(t);
            titoliAggiunti++;
        }
    }

    /**
     * Funzione che estrae dalle risposte dell'API gli oggetti film
     * @param object
     * @return oggetto Film
     * @throws Exception
     */
    private Film estraiFilmDaJSON(JSONObject object) throws Exception {
        int annoPubblicazioneFilm = estraiAnnoDaData(object.getString("release_date"));
        Film t = new Film(object.getInt("id"),
                object.getString("original_title"),
                object.getString("overview"),
                object.getString("poster_path"),
                object.getDouble("vote_average"),
                tmdb.getDurataMinutiFilm(object.getInt("id")),
                annoPubblicazioneFilm
        );
        return t;
    }

    /**
     * Funzione che estrae dalle risposte dell'API gli oggetti serie tv
     * @param object
     * @return oggetto SerieTV
     */
    private SerieTV estraiSerieTVDaJSON(JSONObject object) {
        int annoPubblicazioneSerieTV = estraiAnnoDaData(object.getString("first_air_date"));
        SerieTV t = new SerieTV(object.getInt("id"),
                object.getString("original_name"),
                object.getString("overview"),
                object.getString("backdrop_path"),
                object.getDouble("vote_average"),
                annoPubblicazioneSerieTV
        );
        return t;
    }

    private int estraiAnnoDaData(String dataStr) {
        LocalDate data = LocalDate.parse(dataStr);
        return data.getYear();
    }
}