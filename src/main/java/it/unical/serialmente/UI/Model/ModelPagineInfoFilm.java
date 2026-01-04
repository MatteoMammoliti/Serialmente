package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.WatchlistService;
import it.unical.serialmente.Domain.model.Titolo;


public class ModelPagineInfoFilm {
    private final WatchlistService watchlistService = new WatchlistService();
    public boolean controlloPresenzaTitoloWatchlist(Integer idTitolo) {
        return watchlistService.controlloPresenzaTitoloInListe(idTitolo);
    }

    public void aggiungiFilmInWatchlist(Titolo titolo) throws Exception {
        watchlistService.inserisciTitoloInWatchlist(titolo);
    }
}
