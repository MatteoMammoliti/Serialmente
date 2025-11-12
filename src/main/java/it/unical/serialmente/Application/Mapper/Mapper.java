package it.unical.serialmente.Application.Mapper;

import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.API.TMDbAPI;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Mapper {

    private final TMDbAPI tmdb = new TMDbAPI();

    public ContenitoreDatiProgressoSerie getDatiProgressoSerie(Integer idSerieTV, Integer indexStagione, Integer indexEpisodio) throws Exception {
        String richiesta = "/tv/" + idSerieTV;
        String risposta = tmdb.inviaRichiesta(richiesta);
        JSONObject json = new JSONObject(risposta);
        JSONArray array = json.getJSONArray("seasons");

        JSONObject stagione =  array.getJSONObject(indexStagione);
        Integer idPrimaStagione = stagione.getInt("id");
        String airDate = stagione.optString("air_date", null);
        Integer annoPubblicazione = Integer.parseInt(airDate.substring(0,4));
        Integer numeroProgressivoStagione = stagione.getInt("season_number");

        richiesta = "/tv/" + idSerieTV + "/season/" + numeroProgressivoStagione;
        risposta = tmdb.inviaRichiesta(richiesta);
        json = new JSONObject(risposta);
        array = json.getJSONArray("episodes");
        Integer idPrimoEpisodio = array.getJSONObject(indexEpisodio).getInt("id");
        String descrizioneEpisodio =  array.getJSONObject(indexEpisodio ).optString("overview");
        Integer durataPrimoEpisodio =  array.getJSONObject(indexEpisodio ).getInt("runtime");
        Integer numeroProgressivoEpisodio =  array.getJSONObject(indexEpisodio ).getInt("episode_number");
        return new ContenitoreDatiProgressoSerie(
                idPrimaStagione,
                idPrimoEpisodio,
                annoPubblicazione,
                descrizioneEpisodio,
                durataPrimoEpisodio,
                numeroProgressivoStagione,
                numeroProgressivoEpisodio
        );
    }

    public JSONArray parseEpisodiDiUnaStagione(Integer idSerieTV, Integer numeroProgressivoStagione) throws Exception {
        String risposta =  tmdb.getEpisodiDaStagione(idSerieTV, numeroProgressivoStagione);
        JSONObject json = new JSONObject(risposta);
        return json.getJSONArray("episodes");
    }

    public List<Episodio> parseEpisodiDiUnaStagioneDaJSONArray(JSONArray array) throws Exception {
        List<Episodio> episodi = new  ArrayList<>();
        for(int i = 0; i < array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Episodio ep = new Episodio(
                    obj.getInt("id"),
                    obj.getInt("runtime"),
                    obj.optString("overview")
            );
            episodi.add(ep);
        }
        return episodi;
    }

    public Titolo parseTitoloDaJSON(JSONObject object) throws Exception {
        if(object.has("name")) return  parseSerieTVDaJSON(object);
        if(object.has("title")) return parseFilmDaJSON(object);
        return null;
    }

    public Film parseFilmDaJSON(JSONObject object) throws Exception {
        int annoPubblicazioneFilm = estraiAnnoDaData(object.optString("release_date"));
        return new Film(object.getInt("id"),
                object.optString("title"),
                object.optString("overview"),
                object.optString("poster_path"),
                object.getDouble("vote_average"),
                tmdb.getDurataMinutiFilm(object.getInt("id")),
                annoPubblicazioneFilm
        );
    }

    public SerieTV parseSerieTVDaJSON(JSONObject object) {
        int annoPubblicazioneSerieTV = estraiAnnoDaData(object.optString("first_air_date"));
        return new SerieTV(object.getInt("id"),
                object.optString("name"),
                object.optString("overview"),
                object.optString("poster_path"),
                object.getDouble("vote_average"),
                annoPubblicazioneSerieTV
        );
    }

    public Stagione parseStagioneDaJSON(JSONObject obj) throws Exception {
        Stagione s =  new Stagione(
                obj.optString("name"),
                obj.getInt("id"),
                null
        );
        s.setNumeroStagioneProgressivo(obj.getInt("season_number"));
        return s;
    }

    public Pair<JSONArray, Integer> parseRisultatoPair(String risposta, String index) {
        JSONObject obj = new JSONObject(risposta);
        return new Pair<>(obj.getJSONArray(index), obj.getInt("total_pages"));
    }

    public JSONArray parseRisultato(String risposta, String index) {
        JSONObject obj = new JSONObject(risposta);
        return obj.getJSONArray(index);
    }

    private int estraiAnnoDaData(String dataStr) {
        LocalDate data = LocalDate.parse(dataStr);
        return data.getYear();
    }
}