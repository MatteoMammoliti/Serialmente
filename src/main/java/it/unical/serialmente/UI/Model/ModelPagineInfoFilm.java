package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Application.Service.WatchlistService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.List;

public class ModelPagineInfoFilm {
    private final TitoloService titoloService = new TitoloService();
    private final WatchlistService watchlistService = new WatchlistService();

    public List<Genere> getGeneriFilm(Integer idTitolo) throws Exception {
        return null;
    }

    public boolean controlloPresenzaTitoloWatchlist(Integer idTitolo) throws Exception {
        return watchlistService.controlloPresenzaTitoloInListe(idTitolo);
    }

    public void aggiungiFilmInWatchlist(Titolo titolo) throws Exception {
        watchlistService.inserisciTitoloInWatchlist(titolo);
    }
}
