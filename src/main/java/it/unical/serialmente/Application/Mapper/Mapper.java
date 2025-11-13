package it.unical.serialmente.Application.Mapper;

import it.unical.serialmente.Domain.model.*;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public ContenitoreDatiProgressoSerie getDatiProgressoSerie(String rispostaInfoSerieTV, String rispostaInfoStagione, Integer indexStagione, Integer indexEpisodio) throws Exception {
        JSONObject json = new JSONObject(rispostaInfoSerieTV);
        JSONArray array = json.getJSONArray("seasons");

        JSONObject stagione =  array.getJSONObject(indexStagione);
        Integer idPrimaStagione = stagione.getInt("id");
        String airDate = stagione.optString("air_date", null);
        Integer annoPubblicazione = Integer.parseInt(airDate.substring(0,4));
        Integer numeroProgressivoStagione = stagione.getInt("season_number");

        json = new JSONObject(rispostaInfoStagione);
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

    public List<Episodio> parseEpisodiDiUnaStagione(String risposta) throws Exception {
        JSONObject json = new JSONObject(risposta);
        JSONArray array = json.getJSONArray("episodes");
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

    public Film parseFilmDaJSON(JSONObject object) throws Exception {
        int annoPubblicazioneFilm = estraiAnnoDaData(object.optString("release_date"));
        return new Film(object.getInt("id"),
                object.optString("title"),
                object.optString("overview"),
                object.optString("poster_path"),
                object.getDouble("vote_average"),
                null,
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

    public Integer parseDurataFilm(String risposta) {
        JSONObject json = new JSONObject(risposta);
        return json.getInt("runtime");
    }

    public Integer parseIdProssimoEpisodio(String risposta, Integer idEpisodioAttuale) {
        JSONObject json = new JSONObject(risposta);
        JSONArray arrayRisposta = json.getJSONArray("episodes");
        return getInteger(idEpisodioAttuale, arrayRisposta);
    }

    public Integer parseIdProssimaStagione(String risposta, Integer idStagioneAttuale) {
        JSONObject json = new JSONObject(risposta);
        JSONArray arrayRisposta = json.getJSONArray("seasons");
        return getInteger(idStagioneAttuale, arrayRisposta);
    }

    public Integer parseNumeroEpisodiDaStagione(String risposta) {
        JSONObject obj = new JSONObject(risposta);
        return obj.optInt("number_of_episodes");
    }

    public List<Stagione> parseStagioni(String rispostaStagioni) throws Exception {
        JSONArray stagioniArray = parseRisultato(rispostaStagioni, "seasons");
        List<Stagione> stagioni = new ArrayList<>();
        for(int i = 0; i < stagioniArray.length(); i++){
            Stagione s = parseStagioneDaJSON(stagioniArray.getJSONObject(i));
            stagioni.add(s);
        }
        return stagioni;
    }

    public List<Titolo> parseTitoli(String risposta, String tipologia) throws Exception {
        JSONArray array = parseRisultato(risposta, "results");
        List<Titolo> titoli = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){

            JSONObject object = array.getJSONObject(i);
            Titolo t = switch (tipologia) {
                case "movie" -> parseFilmDaJSON(object);
                case "tv" -> parseSerieTVDaJSON(object);
                default -> null;
            };

            titoli.add(t);
        }
        return titoli;
    }

    private int estraiAnnoDaData(String dataStr) {
        LocalDate data = LocalDate.parse(dataStr);
        return data.getYear();
    }

    private Integer getInteger(Integer id, JSONArray arrayRisposta) {
        for(int i = 0; i < arrayRisposta.length() - 1; i++) {
            JSONObject obj =  arrayRisposta.getJSONObject(i);
            if(obj.getInt("id") == id) {
                return arrayRisposta.getJSONObject(i + 1).getInt("id");
            }
        }
        return null;
    }
}