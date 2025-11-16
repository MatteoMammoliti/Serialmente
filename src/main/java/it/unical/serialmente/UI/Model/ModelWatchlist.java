package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.WatchlistService;
import it.unical.serialmente.Domain.model.Titolo;

import java.sql.SQLException;
import java.util.List;

public class ModelWatchlist {
    private final WatchlistService watchlistService = new WatchlistService();

    public Integer getNumeroStagione(Integer idSerie){
        return watchlistService.getNumeroStagione(idSerie);
    }
    public Integer getNumeroEpisodio(Integer idSerie){
        return watchlistService.getNumeroEpisodio(idSerie);
    }
//    public String getNomeEpisodio(Integer idSerie){
//        return watchlistService.getNomeEpisodio(idSerie);
//    }

    public List<Titolo> getTitoliInWatchlist() throws Exception {
        return watchlistService.restituisciTitoliInWatchlist();
    }
    public void rendiTitoloVisionato(Titolo titolo) throws SQLException {
        watchlistService.rendiFilmVisionato(titolo);
    }
    public void rimuoviTitoloWatchlist(Titolo titolo) throws SQLException {
        watchlistService.rimuoviFilmDallaWatchlist(titolo);
    }
    public void rimuoviSerieWatchlist(Titolo titolo) throws SQLException {
        watchlistService.rimuoviSerieWatchlist(titolo);
    }
    public void episodioSuccessivo(Titolo titolo) throws Exception {
        watchlistService.rendiEpisodioVisionato(titolo);
    }
}
