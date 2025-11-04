package it.unical.serialmente.TechnicalServices.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import it.unical.serialmente.TechnicalServices.Persistence.model.Genere;
import it.unical.serialmente.TechnicalServices.Persistence.model.Piattaforma;
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
    public String getTitoliConsigliati(List<Genere> generi, List<Piattaforma> piattaforme, String tipologia) throws Exception {
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
    public String cercaTitolo(String nomeTitolo, String tipologia, List<Genere> generi, Integer annoPubblicazione) throws Exception {
        if(nomeTitolo != null) return cercaTitoloPerNome(nomeTitolo, tipologia);
        return cercaTitoliPerCriteri(tipologia, generi, annoPubblicazione, null);
    }

    public Integer getDurataMinutiFilm(Integer idFilm) throws Exception {
        String richiesta = "/movie/" + idFilm;
        String risposta = inviaRichiesta(richiesta);

        JSONObject json = new JSONObject(risposta);
        return json.getInt("runtime");
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
        StringBuilder richiesta = new StringBuilder("/search/");

        if(tipologia != null) richiesta.append(tipologia);
        else richiesta.append("multi");

        String nomeTitoloCodificato = URLEncoder.encode(nomeTitolo, StandardCharsets.UTF_8);
        richiesta.append("?query=").append(nomeTitoloCodificato);
        return inviaRichiesta(richiesta.toString());
    }

    /**
     * Funzione che permette di ricercare un film per generi e anno di pubblicazione
     *
     * @param generi
     * @param annoPubblicazione
     * @return String
     * @throws Exception
     */
    private String cercaTitoliPerCriteri(String tipologia, List<Genere> generi, Integer annoPubblicazione, List<Piattaforma> piattaforme) throws Exception {
        StringBuilder richiesta;

        if(tipologia != null) richiesta = new StringBuilder("/discover/" + tipologia);
        else richiesta = new StringBuilder("/discover/multi");

        if(generi != null || annoPubblicazione != null || piattaforme != null) richiesta.append("?");

        boolean generiAggiunti = false;
        if(generi != null) {
            generiAggiunti = true;
            richiesta.append("with_genres=");

            for(int i = 0; i < generi.size(); i++) {
                richiesta.append(generi.get(i).getIdGenere());

                if (i < generi.size() - 1) {
                    richiesta.append(",");
                }
            }
        }

        boolean piattaformeAggiunte = false;
        if(generiAggiunti) { richiesta.append("&"); }

        if(piattaforme != null) {
            piattaformeAggiunte = true;
            richiesta.append("with_watch_providers=");

            for(int i = 0; i < piattaforme.size(); i++) {
                richiesta.append(piattaforme.get(i).getIdPiattaforma());

                if (i < piattaforme.size() - 1) {
                    richiesta.append(",");
                }
            }
        }

        if(piattaformeAggiunte) { richiesta.append("&"); }

        if(annoPubblicazione != null) {
            richiesta.append("year=").append(annoPubblicazione);
        }

        return inviaRichiesta(richiesta.toString());
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