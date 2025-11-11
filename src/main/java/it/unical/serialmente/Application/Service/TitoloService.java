package it.unical.serialmente.Application.Service;

import it.unical.serialmente.TechnicalServices.API.TMDbAPI;
import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.PreferisceGenereDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.PreferiscePiattaformaDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.ProgressoSerieDAOPostgres;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TitoloService {

    private final TMDbAPI tmdb = new TMDbAPI();
    private final PreferisceGenereDAOPostgres preferisceGenereDao = new PreferisceGenereDAOPostgres(DBManager.getInstance().getConnection());
    private final PreferiscePiattaformaDAOPostgres preferiscePiattaformaDao = new  PreferiscePiattaformaDAOPostgres(DBManager.getInstance().getConnection());
    private final ProgressoSerieDAOPostgres progressoSerieDao = new ProgressoSerieDAOPostgres(DBManager.getInstance().getConnection());


    public List<Titolo> getTitoliPerGenere(Genere g, String tipologia) throws Exception {
        String risposta = tmdb.getTitoliPerGenere(g, tipologia);
        JSONObject obj = new JSONObject(risposta);
        JSONArray jsonArray = obj.getJSONArray("results");

        List<Titolo> titoliPerGenere = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            switch (tipologia) {
                case "movie":
                    titoliPerGenere.add(estraiFilmDaJSON(jsonObject));
                    break;

                case "tv":
                    titoliPerGenere.add(estraiSerieTVDaJSON(jsonObject));
                    break;
            }
        }
        return titoliPerGenere;
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
                    obj.optString("name"),
                    obj.getInt("id"),
                    getEpisodi(idSerieTV, obj.getInt("season_number"))
            );
            s.setNumeroStagioneProgressivo(obj.getInt("season_number"));
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
                    obj.optString("overview")
            );
            episodi.add(ep);
        }
        return episodi;
    }

    /**
     * Funzione che restituisce un massimo di tot di titoli tra quelli consigliati
     * @param generi
     * @param piattaforme
     * @param tipologiaTitolo
     * @return
     * @throws Exception
     */
     public List<Titolo> getTitoliConsigliati(List<Genere> generi, List<Piattaforma> piattaforme, String tipologiaTitolo) throws Exception {
         String risposta = tmdb.getTitoliConsigliati(generi, piattaforme, tipologiaTitolo);
         List<Titolo> titoli = new ArrayList<>();
         estraiPiuVistiConsigliati(risposta, titoli, tipologiaTitolo);

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

            if(object.has("name")) { t = estraiSerieTVDaJSON(object); }
            if(object.has("title")) { t = estraiFilmDaJSON(object); }

            titoli.add(t);
        }
        return titoli;
    }

    /**
     * Funzione che restituisce un massimo di 10 titoli (5 serie tv e 5 film) tra quelli più visti
     * @param tipologiaTitolo
     * @return List<Titolo> di titoli più visti
     */
    public List<Titolo> getTitoliPiuVisti(String tipologiaTitolo) throws Exception {
        String risposta = tmdb.getTitoliConSort(tipologiaTitolo, "popularity.desc");

        List<Titolo> titoli = new ArrayList<>();
        estraiPiuVistiConsigliati(risposta, titoli, tipologiaTitolo);
        return titoli;
    }

    /**
     * Funzione che restituisce un elenco di titoli tra quelli usciti di recente
     * @param tipologia
     * @return
     * @throws Exception
     */
    public List<Titolo> getTitoliNovita(String tipologia) throws Exception {

        if(!tipologia.equals("movie") && !tipologia.equals("tv")) {
            throw new IllegalArgumentException("Tipologia non valida: " + tipologia);
        }

        String risposta = switch (tipologia) {
            case "movie" -> tmdb.getTitoliConSort(tipologia, "primary_release_date.desc");
            case "tv" -> tmdb.getTitoliConSort(tipologia, "first_air_date.desc");
            default -> "";
        };

        List<Titolo> titoli = new ArrayList<>();
        estraiPiuVistiConsigliati(risposta, titoli, tipologia);
        return titoli;
    }

    public void popolaListaSerieTV(List<Titolo> titoli) throws Exception {

        for (Titolo titolo : titoli) {

            if (titolo.getTipologia().equals("SerieTv")) {

                List<Stagione> stagioni = getStagioni(titolo.getIdTitolo());
                SerieTV s = (SerieTV) titolo;

                for (Stagione stagione : stagioni) {
                    List<Episodio> episodi = getEpisodi(titolo.getIdTitolo(), stagione.getNumeroStagioneProgressivo());
                    stagione.setEpisodi(episodi);
                }
                s.setStagioni(stagioni);
            }
        }
    }

    public void rendiEpisodiVistiSerieTV(List<Titolo> titoli) throws Exception {
        for (Titolo titolo : titoli) {
            if (titolo.getTipologia().equals("SerieTv")) {
                SerieTV s = (SerieTV) titolo;

                ContenitoreDatiProgressoSerie c = progressoSerieDao.getDatiCorrenti(
                        SessioneCorrente.getUtenteCorrente().getIdUtente(),
                        s
                );

                for (int k = 0; k < s.getStagioni().size(); k++) {
                    Stagione stagione = s.getStagioni().get(k);

                    for (int j = 0; j < stagione.getEpisodi().size(); j++) {
                        if (!Objects.equals(stagione.getEpisodi().get(j).getIdEpisodio(), c.idEpisodio)) {
                            stagione.getEpisodi().get(j).setVisualizzato(true);
                        } else return;
                    }

                    stagione.setCompletata(true);
                }
            }
        }
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

            if(titoliAggiunti == 200){ return; }
            JSONObject object = array.getJSONObject(i);
            Titolo t = switch (tipologia) {
                case "movie" -> estraiFilmDaJSON(object);
                case "tv" -> estraiSerieTVDaJSON(object);
                default -> null;
            };

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

    /**
     * Funzione che estrae dalle risposte dell'API gli oggetti serie tv
     * @param object
     * @return oggetto SerieTV
     */
    private SerieTV estraiSerieTVDaJSON(JSONObject object) {
        int annoPubblicazioneSerieTV = estraiAnnoDaData(object.optString("first_air_date"));
        return new SerieTV(object.getInt("id"),
                object.optString("name"),
                object.optString("overview"),
                object.optString("poster_path"),
                object.getDouble("vote_average"),
                annoPubblicazioneSerieTV
        );
    }

    private int estraiAnnoDaData(String dataStr) {
        LocalDate data = LocalDate.parse(dataStr);
        return data.getYear();
    }
}