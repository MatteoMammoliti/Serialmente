package it.unical.serialmente.UI.Model;


import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Application.Service.WatchlistService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;

import java.sql.SQLException;
import java.util.List;

public class ModelPagineInfoSerieTv {
    private final WatchlistService watchlistService = new WatchlistService();
    private final TitoloService titoloService = new TitoloService();


    public boolean controlloSeSerieInListe(Integer idSerie){
        return watchlistService.controlloPresenzaSerieTvInListe(idSerie);
    }
    public void aggiungiSerieTvInWatchlist(Titolo serie) throws SQLException {
        watchlistService.inserisciTitoloInWatchlist(serie);
    }
    public List<Genere> getGeneriSerie(Integer idSerie) throws Exception {
        return null;
    }
}
