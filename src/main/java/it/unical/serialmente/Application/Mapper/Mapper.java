package it.unical.serialmente.Application.Mapper;

import it.unical.serialmente.Domain.model.*;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.*;

public class Mapper {

    /**
     * Data una stringa Json, estrapola tutti gli episodi di una stagione.
     * @param risposta Stringa risposta Json che arriva dall'API
     * @return Una lista contentenente tutti gli episodi di una stagione di una Serie Tv.
     */
    public List<Episodio> parseEpisodiDiUnaStagione(String risposta) {
        JSONObject json = new JSONObject(risposta);
        JSONArray array = json.getJSONArray("episodes");
        List<Episodio> episodi = new  ArrayList<>();
        for(int i = 0; i < array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            Episodio ep = new Episodio(
                    obj.optInt("id"),
                    obj.optInt("runtime")
            );
            episodi.add(ep);
        }
        return episodi;
    }

    /**
     * Dato un JSOnObject che racchiude il film, estrae l'oggetto Film.
     * @param object Oggetto Json proveniente dall'API che racchiude il Film.
     * @return Oggetto Film estratto dall'oggetto Json
     */
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

    /**
     * Dato un JSOnObject che racchiude la SerieTv, estrae l'oggetto SerieTv.
     * @param object Oggetto Json proveniente dall'API che racchiude la SerieTv.
     * @return Oggetto SerieTv estratto dall'oggetto Json
     */
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

    /**
     * Dato un JSOnObject che racchiude la stagione di una SerieTv, estrae l'oggetto Stagione.
     * @param obj Oggetto Json proveniente dall'API che racchiude la stagione di una SerieTv.
     * @return Oggetto Stagione estratto dall'oggetto Json
     */
    public Stagione parseStagioneDaJSON(JSONObject obj){
        Stagione s =  new Stagione(
                obj.optString("name"),
                obj.optInt("id"),
                null
        );
        s.setNumeroStagioneProgressivo(obj.optInt("season_number"));
        return s;
    }

    /**
     * Data una risposta Json di un Film, estraggo la durata del film.
     * @param risposta risposta Json data dall'API
     * @return Intero che rappresenta la durata del film in minuti
     */
    public Integer parseDurataFilm(String risposta) {
        JSONObject json = new JSONObject(risposta);
        return json.optInt("runtime");
    }

    /**
     * Data una risposta Json di un'intera Serie Tv, calcola e restituisce l'id del prossimo episodio
     * @param risposta Risposta Json contentente l'intera serie Tv data dall'APi.
     * @param idEpisodioAttuale id dell'episodio attuale.
     * @return Id del prossimo episodio, 0 se non ne esiste uno.
     */
    public Pair<Integer, Integer> parseIdProssimoEpisodio(String risposta, Integer idEpisodioAttuale) {
        JSONObject json = new JSONObject(risposta);
        JSONArray arrayRisposta = json.getJSONArray("episodes");
        return getInteger(idEpisodioAttuale, arrayRisposta, null);
    }
    /**
     * Data una risposta Json di un'intera Serie Tv, calcola e restituisce l'id della prossima stagione.
     * @param risposta Risposta Json contentente l'intera serie Tv data dall'APi.
     * @param idStagioneAttuale id della stagione attuale.
     * @return Id della prossima stagione, 0 se non ne esiste uno.
     */
    public Pair<Integer, Integer> parseIdProssimaStagione(String risposta, Integer idStagioneAttuale) {
        JSONObject json = new JSONObject(risposta);
        JSONArray arrayRisposta = json.getJSONArray("seasons");
        return getInteger(idStagioneAttuale, arrayRisposta, "tv");
    }

    /**
     * Data una stringa Json, estrapola tutte le stagioni di una SerieTv.
     * @param rispostaStagioni Stringa risposta Json che arriva dall'API
     * @return Una lista contentenente tutte le stagioni di una Serie Tv.
     */
    public List<Stagione> parseStagioni(String rispostaStagioni) {
        JSONObject obj = new JSONObject(rispostaStagioni);
        JSONArray stagioniArray = obj.getJSONArray("seasons");
        List<Stagione> stagioni = new ArrayList<>();
        for(int i = 0; i < stagioniArray.length(); i++){
            Stagione s = parseStagioneDaJSON(stagioniArray.getJSONObject(i));
            stagioni.add(s);
        }
        return stagioni;
    }

    /**
     * In base alla tipologia passata, la risposta contiene titoli di quella tipologia.Se la tipologia è null
     * allora i titoli sono "mischiati".
     * @param risposta Json contenente i titoli.
     * @param tipologia "SerieTv","Film", o null.
     * @return Lista con tutti i titoli ricercati.
     */
    public List<Titolo> parseTitoli(String risposta, String tipologia) {
        JSONObject obj = new JSONObject(risposta);
        JSONArray array = obj.getJSONArray("results");
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

    /**
     * Dato un titolo, restituisce tutti i generi appartenenti a quel titolo.
     * @param risposta json che contiene il titolo.
     * @return Lista di generi di quel titolo
     */

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

    /**
     * Data un Json contenente un Titolo, restituisce le piattaforme su cui è distribuito.
     * @param risposta Json contenente il titolo
     * @param includeNonFlatrate True se voglio includere le piattaforme che mettono il film come noleggiabile o acquistabile.
     * @return lista di piattaforme su cui il titolo è distribuito.
     */
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

    /**
     * Aggiunge nella mappa le piattaforme dell'array Json che viene passato come parametro.
     * @param arr Json che contiene le pittaforme dello specifico paese.
     * @param out Mappa che contiene come chiave l'id della piattaforma e come valore la piattaforma di riferimento.
     */
    private void aggiungiPiattaformeDaArray(JSONArray arr, Map<Integer, Piattaforma> out) {
        if (arr == null) return;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject p = arr.getJSONObject(i);

            int id = p.optInt("provider_id");
            if (id == 0) continue;

            out.computeIfAbsent(id, k -> new Piattaforma(p.optString("provider_name"), id));
        }
    }

    /**
     * Data una stringa rappresentante una data, la converte in un LocalDate e restituisce l'anno.
     * @param dataStr Data in formato stringa da convertire.
     * @return Intero che rappresenta l'anno della data.
     */
    private int estraiAnnoDaData(String dataStr) {
        LocalDate data = LocalDate.parse(dataStr);
        return data.getYear();
    }

    /**
     * Dato l'id dell'episodio o della stagione attuale, il Json contenente l'array di episodi o di stagioni
     * estrae l'id del prossimo episodio o della prossima stagione.
     * @param id Id dell'episodio/stagione attuale
     * @param arrayRisposta json contenente l'array di episodi/stagioni
     * @param tipologia "SerieTv" o null.
     * @return Un pair con chiave l'id della prossima stagione o episodio e come valore la durata solamente se sto estraendo
     * l'id del prossimo episodio.
     */
    private Pair<Integer, Integer> getInteger(Integer id, JSONArray arrayRisposta, String tipologia) {

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