package it.unical.serialmente.TechnicalServices.API;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class TMDbRequest {

    private static final String BASE = "https://api.themoviedb.org/3";

    public String getTitoliPerGenere(Integer idGenere, String tipologia, String pagina) {
        if (pagina.isEmpty())
            return BASE + "/discover/" + tipologia + "?with_genres=" + idGenere;
        return BASE + "/discover/" + tipologia + "?with_genres=" + idGenere + pagina;
    }

    public String getTitolo(Integer idTitolo, String tipologia) {
        return BASE + "/" + tipologia + "/" + idTitolo;
    }

    public String getPiattaforme(Integer idTitolo, String tipologia) {
        return BASE + "/" + tipologia + "/" + idTitolo + "/watch/providers";
    }

    public String getTitoliCasuali(String tipologia, Integer pagina) {
        String url;
        url = switch (tipologia) {
            case "movie" -> BASE + "/discover/" + tipologia + "?sort_by=primary_release_date.asc";
            case "tv" -> BASE + "/discover/" + tipologia + "?sort_by=first_air_date.asc";
            default -> throw new IllegalStateException("Unexpected value: " + tipologia);
        };

        if(pagina == null) return url;
        return url + "&page=" + pagina;
    }

    public String getEpisodiDaStagione(Integer numeroStagione, Integer idSerieTV) {
        return BASE + "/tv/" + idSerieTV + "/season/" + numeroStagione;
    }

    public String cercaTitoloPerNome(String nomeTitolo, String tipologia) {
        StringBuilder richiesta = new StringBuilder(BASE + "/search/");
        if (tipologia != null) richiesta.append(tipologia);
        else richiesta.append("multi");

        String nomeTitoloCodificato =
                URLEncoder.encode(nomeTitolo, StandardCharsets.UTF_8);

        richiesta.append("?query=").append(nomeTitoloCodificato);

        return richiesta.toString();
    }

    public String getDurataFilm(Integer idFilm) {
        return BASE + "/movie/" + idFilm;
    }

    public String getTitoliConSort(String tipologia, String tipologiaSort, String pagina) {

        switch (tipologiaSort) {
            case "popularity.desc":
                return BASE + "/discover/" + tipologia
                        + "?sort_by=" + tipologiaSort + pagina;

            case "primary_release_date.desc":
                return BASE + "/" + tipologia + "/now_playing"
                        + "?region=IT" + pagina;

            case "first_air_date.desc":
                LocalDate ora = LocalDate.now();
                LocalDate dataInizio = ora.minusMonths(3);
                LocalDate dataFine = ora.plusDays(7);

                return BASE + "/discover/" + tipologia
                        + "?sort_by=" + tipologiaSort
                        + "&first_air_date.gte=" + dataInizio
                        + "&first_air_date.lte=" + dataFine
                        + "&with_original_language=en"
                        + pagina;
        }

        return null;
    }

    public String cercaTitoliPerCriteri(String tipologia, List<Genere> generi,
                                        Integer annoPubblicazione, List<Piattaforma> piattaforme,
                                        String pagina) {

        StringBuilder richiesta = new StringBuilder(BASE);

        if (tipologia != null) richiesta.append("/discover/").append(tipologia);
        else richiesta.append("/discover/multi");

        boolean hasQuery = generi != null || annoPubblicazione != null || piattaforme != null;
        if (hasQuery) richiesta.append("?");

        boolean generiAggiunti = false;

        if (generi != null) {
            generiAggiunti = true;
            richiesta.append("with_genres=");

            for (int i = 0; i < generi.size(); i++) {
                richiesta.append(generi.get(i).getIdGenere());
                if (i < generi.size() - 1) richiesta.append("|");
            }
        }

        if (generiAggiunti) richiesta.append("&");

        boolean piattaformeAggiunte = false;

        if (piattaforme != null) {
            piattaformeAggiunte = true;
            richiesta.append("with_watch_providers=");

            for (int i = 0; i < piattaforme.size(); i++) {
                richiesta.append(piattaforme.get(i).getIdPiattaforma());
                if (i < piattaforme.size() - 1) richiesta.append("|");
            }
        }

        if (piattaformeAggiunte && annoPubblicazione != null)
            richiesta.append("&year=").append(annoPubblicazione);

        if (pagina != null)
            richiesta.append("&page=2");

        return richiesta.toString();
    }
}
