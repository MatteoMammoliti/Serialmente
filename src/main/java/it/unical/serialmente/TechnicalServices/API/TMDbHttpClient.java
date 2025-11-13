package it.unical.serialmente.TechnicalServices.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TMDbHttpClient {

    private static final String API_KEY = "af7cc46a092adabb4bbffcef3ee8304b";
    private static final String DEFAULT_LANGUAGE = "it-IT";

    public String richiesta(String urlString) throws Exception {
        urlString = appendDefaults(urlString);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Errore chiamata TMDb: codice = " + responseCode);
        }

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

            StringBuilder risposta = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                risposta.append(linea);
            }

            return risposta.toString();
        }
        finally {
            connection.disconnect();
        }
    }

    private String appendDefaults(String url) {
        String params = "api_key=" + API_KEY + "&language=" + DEFAULT_LANGUAGE;

        if (url.contains("?"))
            return url + "&" + params;
        else
            return url + "?" + params;
    }
}