package it.unical.serialmente.TechnicalServices.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.*;

public class TMDbAPI {

    private final String API_KEY = "af7cc46a092adabb4bbffcef3ee8304b";
    private final String BASE_URL = "https://api.themoviedb.org/3";
    private final String DEFAULT_LANGUAGE = "it-IT";
    private final String RICHIESTA_DATI = "GET";
    private Map<String, Integer> generi = new HashMap<>();

    /**
     * Funzione che consente di ricercare un titolo specifico
     *
     * @param nomeTitolo
     * @param tipologia
     * @param generi
     * @param annoPubblicazione
     * @return String
     * @throws Exception
     */
    public String cercaTitolo(String nomeTitolo, String tipologia, List<String> generi, Integer annoPubblicazione) throws Exception {
        if(nomeTitolo != null) return cercaTitoloPerNome(nomeTitolo, tipologia);
        return cercaTitoliPerCriteri(tipologia, getIdGeneri(generi, tipologia), annoPubblicazione);
    }

    /**
     * Funzione di utility per convertire la lista di generi da stringhe ad id numerici
     *
     * @param generi
     * @param tipologia
     * @return List<Integer> idGeneri
     * @throws Exception
     */
    private List<Integer> getIdGeneri(List<String> generi, String tipologia) throws Exception {
        List<Integer> idGeneri = new ArrayList<>();
        Map<String, Integer> mappaIdGeneri = new HashMap<>();

        String richiesta = "/genre/" + tipologia + "/list";
        String risposta = inviaRichiesta(richiesta);

        JSONObject json =  new JSONObject(risposta);
        JSONArray jsonArray = json.getJSONArray("genres");

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject genre = jsonArray.getJSONObject(i);
            mappaIdGeneri.put(genre.getString("name"), genre.getInt("id"));
        }

        for(int i = 0; i < generi.size(); i++){
            idGeneri.add(mappaIdGeneri.get(generi.get(i)));
        }
        return idGeneri;
    }

    /**
     * Funzione che consente di ricercare un titolo per il proprio nome
     *
     * @param nomeTitolo
     * @param tipologia
     * @return
     * @throws Exception
     */
    private String cercaTitoloPerNome(String nomeTitolo, String tipologia) throws Exception {
        String richiesta = "/search/";

        if(tipologia != null) richiesta = richiesta + tipologia;
        else richiesta = richiesta + "multi";

        String nomeTitoloCodificato = URLEncoder.encode(nomeTitolo, StandardCharsets.UTF_8);
        richiesta = richiesta + "?query=" + nomeTitoloCodificato;
        return inviaRichiesta(richiesta);
    }

    /**
     * Funzione che permette di ricercare un film per generi e anno di pubblicazione
     *
     * @param generi
     * @param annoPubblicazione
     * @return String
     * @throws Exception
     */
    private String cercaTitoliPerCriteri(String tipologia, List<Integer> generi, Integer annoPubblicazione) throws Exception {
        String richiesta;

        if(tipologia != null) richiesta = "/discover/" + tipologia;
        else richiesta = "/discover/multi";

        String richiestaConAggiunteGeneri = richiesta + "?with_genres=";

        boolean generiAggiunti = false;
        if(generi != null) generiAggiunti = true;

        for(int i = 0; i < generi.size(); i++) {
            richiestaConAggiunteGeneri += generi.get(i);

            if(i < generi.size()-1) { richiestaConAggiunteGeneri += ","; }
        }

        if(generiAggiunti) {
            richiesta = richiestaConAggiunteGeneri;

            if(annoPubblicazione != null) richiesta += "&year=" + annoPubblicazione;
            else return inviaRichiesta(richiesta);

        } else { richiesta = richiesta + "?year=" +  annoPubblicazione; }

        return inviaRichiesta(richiesta);
    }

    /**
     * Funzione di ausilio che invia richieste HTTP GET a TMDb
     *
     * @param richiesta
     * @return JSON
     * @throws Exception
     */
    private String inviaRichiesta(String richiesta) throws Exception {
        URL url = new URL(costruisciURL(richiesta));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(RICHIESTA_DATI);
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (!(responseCode == HttpURLConnection.HTTP_OK)) {
            throw new RuntimeException("Errore nella chiamata a TMDb con codice " + responseCode);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

            String risposta = "";
            String linea = reader.readLine();

            while (linea != null) {
                risposta += linea;
                linea = reader.readLine();
            }
            return risposta;

        }  finally {
            connection.disconnect();
        }
    }

    private String costruisciURL(String richiesta) {
        String urlString = BASE_URL + richiesta;
        String daAggiungere = "api_key=" + API_KEY + "&language=" + DEFAULT_LANGUAGE;

        if(richiesta.contains("?")) { urlString = urlString + "&" + daAggiungere; }
        else  { urlString = urlString + "?" + daAggiungere; }

        return urlString;
    }
}
