package it.unical.serialmente.Application.Mapper;

import it.unical.serialmente.Domain.model.*;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.*;

public class Mapper {

//    public ContenitoreDatiProgressoSerie getDatiProgressoSerie(String rispostaInfoSerieTV, String rispostaInfoStagione, Integer indexStagione, Integer indexEpisodio) throws Exception {
//        JSONObject json = new JSONObject(rispostaInfoSerieTV);
//        JSONArray array = json.getJSONArray("seasons");
//
//        JSONObject stagione =  array.getJSONObject(indexStagione);
//        Integer idPrimaStagione = stagione.optInt("id");
//        String airDate = stagione.optString("air_date", null);
//        Integer annoPubblicazione = Integer.parseInt(airDate.substring(0,4));
//        Integer numeroProgressivoStagione = stagione.optInt("season_number");
//
//        json = new JSONObject(rispostaInfoStagione);
//        array = json.getJSONArray("episodes");
//        Integer idPrimoEpisodio = array.getJSONObject(indexEpisodio).optInt("id");
//        String descrizioneEpisodio =  array.getJSONObject(indexEpisodio ).optString("overview");
//        Integer durataPrimoEpisodio =  array.getJSONObject(indexEpisodio ).optInt("runtime");
//        Integer numeroProgressivoEpisodio =  array.getJSONObject(indexEpisodio ).optInt("episode_number");
//        return new ContenitoreDatiProgressoSerie(
//                idPrimaStagione,
//                idPrimoEpisodio,
//                annoPubblicazione,
//                descrizioneEpisodio,
//                durataPrimoEpisodio,
//                numeroProgressivoStagione,
//                numeroProgressivoEpisodio
//        );
//    }

    public List<Episodio> parseEpisodiDiUnaStagione(String risposta) {
        JSONObject json = new JSONObject(risposta);
        JSONArray array = json.getJSONArray("episodes");
        List<Episodio> episodi = new  ArrayList<>();
        for(int i = 0; i < array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Episodio ep = new Episodio(
                    obj.optInt("id"),
                    obj.optInt("runtime"),
                    obj.optString("overview")
            );
            episodi.add(ep);
        }
        return episodi;
    }

    public Film parseFilmDaJSON(JSONObject object) {

        int annoPubblicazioneFilm = 0;
        String annoParserizzato = object.optString("release_date");

        if(!Objects.equals(annoParserizzato, "")) {
            annoPubblicazioneFilm = estraiAnnoDaData(annoParserizzato);
        }

        return new Film(object.optInt("id"),
                object.optString("title"),
                object.optString("overview"),
                object.optString("poster_path"),
                object.optDouble("vote_average"),
                object.optInt("runtime"),
                annoPubblicazioneFilm
        );
    }

    public SerieTV parseSerieTVDaJSON(JSONObject object) {

        int annoPubblicazioneSerieTV = 0;
        String annoParserizzato = object.optString("first_air_date");
        if(!Objects.equals(annoParserizzato, "")){
            annoPubblicazioneSerieTV = estraiAnnoDaData(annoParserizzato);
        }

        return new SerieTV(object.optInt("id"),
                object.optString("name"),
                object.optString("overview"),
                object.optString("poster_path"),
                object.optDouble("vote_average"),
                annoPubblicazioneSerieTV
        );
    }

    public Stagione parseStagioneDaJSON(JSONObject obj) throws Exception {
        Stagione s =  new Stagione(
                obj.optString("name"),
                obj.optInt("id"),
                null
        );
        s.setNumeroStagioneProgressivo(obj.optInt("season_number"));
        return s;
    }

    public Pair<JSONArray, Integer> parseRisultatoPair(String risposta, String index) {
        JSONObject obj = new JSONObject(risposta);
        return new Pair<>(obj.getJSONArray(index), obj.optInt("total_pages"));
    }

    public JSONArray parseRisultato(String risposta, String index) {
        JSONObject obj = new JSONObject(risposta);
        return obj.getJSONArray(index);
    }

    public Integer parseDurataFilm(String risposta) {
        JSONObject json = new JSONObject(risposta);
        return json.optInt("runtime");
    }

    public Pair<Integer, Integer> parseIdProssimoEpisodio(String risposta, Integer idEpisodioAttuale) {
        JSONObject json = new JSONObject(risposta);
        JSONArray arrayRisposta = json.getJSONArray("episodes");
        return getInteger(idEpisodioAttuale, arrayRisposta, null);
    }

    public Pair<Integer, Integer> parseIdProssimaStagione(String risposta, Integer idStagioneAttuale) {
        JSONObject json = new JSONObject(risposta);
        JSONArray arrayRisposta = json.getJSONArray("seasons");
        return getInteger(idStagioneAttuale, arrayRisposta, "tv");
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

            if (tipologia == null) {
                tipologia = object.optString("media_type");
            }

            Titolo t = switch (tipologia) {
                case "movie" -> parseFilmDaJSON(object);
                case "tv" -> parseSerieTVDaJSON(object);
                default -> null;
            };

            titoli.add(t);
        }
        return titoli;
    }

//    public Pair<Integer, Integer> parseStatistiche(String risposta) {
//        JSONObject obj = new JSONObject(risposta);
//        int numEpisodi = obj.optInt("number_of_episodes");
//
//        int durataEpisodi = 0;
//        JSONArray durataMedia =  obj.getJSONArray("episode_run_time");
//
//        for(int i = 0; i < durataMedia.length(); i++){
//            durataEpisodi += durataMedia.getInt(i);
//        }
//        if(!durataMedia.isEmpty())
//            durataEpisodi = durataEpisodi / durataMedia.length();
//
//        return new Pair<>(numEpisodi, durataEpisodi * numEpisodi);
//    }

    public List<Genere> parseGeneri(String risposta) {
        JSONObject obj = new JSONObject(risposta);

        if (!obj.has("genres")) return List.of();

        JSONArray generiArray = obj.getJSONArray("genres");

        List<Genere> generi = new ArrayList<>();

        for(int  i = 0; i < generiArray.length(); i++){
            JSONObject object = generiArray.getJSONObject(i);
            generi.add(
                    new Genere(object.optString("name"), object.optInt("id"))
            );
        }
        return generi;
    }

    public List<Piattaforma> parsePiattaforme(String risposta, boolean includeNonFlatrate) {
        JSONObject root = new JSONObject(risposta);
        JSONObject results = root.optJSONObject("results");
        if (results == null) return List.of();

        String[] paesi = {"IT", "DE", "FR", "US", "GB", "ES"};

        Map<Integer, Piattaforma> piattaformeUniche = new HashMap<>();

        for (String paese : paesi) {
            JSONObject countryObj = results.optJSONObject(paese);
            if (countryObj == null) continue;

            JSONArray flatrate = countryObj.optJSONArray("flatrate");
            aggiungiPiattaformeDaArray(flatrate, piattaformeUniche);

            if (includeNonFlatrate) {
                JSONArray rent = countryObj.optJSONArray("rent");
                JSONArray buy = countryObj.optJSONArray("buy");
                aggiungiPiattaformeDaArray(rent, piattaformeUniche);
                aggiungiPiattaformeDaArray(buy, piattaformeUniche);
            }
        }

        return new ArrayList<>(piattaformeUniche.values());
    }

    public Integer parsePagineTotali(String risposta) {
        JSONObject obj = new JSONObject(risposta);
        return Math.min(obj.optInt("total_pages"), 500);
    }

    private void aggiungiPiattaformeDaArray(JSONArray arr, Map<Integer, Piattaforma> out) {
        if (arr == null) return;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject p = arr.getJSONObject(i);

            int id = p.optInt("provider_id");
            if (id == 0) continue;

            out.computeIfAbsent(id, k -> new Piattaforma(p.optString("provider_name"), id));
        }
    }

    private int estraiAnnoDaData(String dataStr) {
        LocalDate data = LocalDate.parse(dataStr);
        return data.getYear();
    }

    private Pair<Integer, Integer> getInteger(Integer id, JSONArray arrayRisposta, String tipologia) {

        System.out.println("CONTROLLO ID: " + id);
        System.out.println("Risposta: " + arrayRisposta);

        if (arrayRisposta == null || arrayRisposta.isEmpty()) {
            return new Pair<>(0, 0);
        }

        if (id == null || id == 0) {
            JSONObject primoEpisodioProssimaStagione = arrayRisposta.getJSONObject(0);

            int prossimoId = primoEpisodioProssimaStagione.optInt("id");

            Integer runtime = (tipologia == null)
                    ? primoEpisodioProssimaStagione.optInt("runtime")
                    : null;

            return new Pair<>(prossimoId, runtime);
        }

        for (int i = 0; i < arrayRisposta.length(); i++) {

            JSONObject curr = arrayRisposta.getJSONObject(i);

            if (curr.optInt("id") == id) {

                boolean ultimo = (i == arrayRisposta.length() - 1);

                if (!ultimo) {
                    JSONObject next = arrayRisposta.getJSONObject(i + 1);
                    int nextId = next.optInt("id");
                    Integer runtime = (tipologia == null)
                            ? next.optInt("runtime")
                            : null;
                    return new Pair<>(nextId, runtime);
                }

                Integer runtime = (tipologia == null)
                        ? curr.optInt("runtime")
                        : null;
                return new Pair<>(0, runtime);
            }
        }
        return new Pair<>(0, 0);
    }
}