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

    /**
     * Funzione usata per ottenere film e serietv da consigliare lall'utente nella homepage
     *
     * @param generi
     * @param piattaforme
     * @return
     */
    public String getTitoliConsigliati(List<Integer> generi, List<Integer> piattaforme, String tipologia) throws Exception {
        return cercaTitoliPerCriteri(tipologia, generi, null, piattaforme);
    }


    /**
     * Funzione che permette di ottenere il titolo di tipologia {tipologia} ordinati per {tipologiaSort}:
     * - popolarità = popularity.desc
     * - ultime uscite = air_date.desc
     *
     * @param tipologia
     * @param tipologiaSort
     * @return Titolo
     * @throws Exception
     */
    public String getTitoliPiùVisti(String tipologia, String tipologiaSort) throws Exception {
        String richiesta = "/discover/" + tipologia + "?sort_by=" +  tipologiaSort;
        return inviaRichiesta(richiesta);
    }

    /**
     * Restituisce i dettagli della SerieTV associata ad {idSerie}. L'{idSerie} è quello del DB remoto
     *
     * @param idSerie
     * @return
     * @throws Exception
     */
    public String getSerieTV(Integer idSerie) throws Exception {
        String richiesta = "/tv/"+ idSerie;
        return inviaRichiesta(richiesta);
    }

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
        return cercaTitoliPerCriteri(tipologia, getIdGeneri(generi, tipologia), annoPubblicazione, null);
    }

    /**
     * Funzione che restituisce le piattaforme streaming dei titoli di tipologia {tipologia}
     * @param tipologia
     * @return listaPiattaforme
     * @throws Exception
     */
    public List<String> getPiattaformeDisponibili(String tipologia) throws Exception {
        String richiesta = "/watch/providers/" +  tipologia;
        String risposta = inviaRichiesta(richiesta);

        JSONObject json = new JSONObject(risposta);
        JSONArray jsonArray = json.getJSONArray("results");
        List<String> lista = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){
            lista.add(jsonArray.getJSONObject(i).getString("provider_name"));
        }
        return lista;
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
    private String cercaTitoliPerCriteri(String tipologia, List<Integer> generi, Integer annoPubblicazione, List<Integer> piattaforme) throws Exception {
        String richiesta;

        if(tipologia != null) richiesta = "/discover/" + tipologia;
        else richiesta = "/discover/multi";

        if(generi != null || annoPubblicazione != null || piattaforme != null) richiesta = richiesta + "?";

        boolean generiAggiunti = false;
        if(generi != null) {
            generiAggiunti = true;
            richiesta = richiesta + "with_genres=";

            for(int i = 0; i < generi.size(); i++) {
                richiesta += generi.get(i);

                if (i < generi.size() - 1) {
                    richiesta += ",";
                }
            }
        }

        boolean piattaformeAggiunte = false;
        if(generiAggiunti) { richiesta = richiesta + "&"; }

        if(piattaforme != null) {
            piattaformeAggiunte = true;
            richiesta = richiesta + "with_watch_providers=";

            for(int i = 0; i < piattaforme.size(); i++) {
                richiesta += piattaforme.get(i);

                if (i < piattaforme.size() - 1) {
                    richiesta += ",";
                }
            }
        }

        if(piattaformeAggiunte) { richiesta = richiesta + "&"; }

        if(annoPubblicazione != null) {
            richiesta = richiesta + "year=" +  annoPubblicazione;
        }

        return inviaRichiesta(richiesta);
    }

    /**
     * Funzione di utility che invia richieste HTTP GET a TMDb
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