package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.PreferenzeService;
import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.ArrayList;
import java.util.List;

public class ModelSezioneFilm {
    private TitoloService titoloService = new TitoloService();
    private PreferenzeService preferenzeService = new PreferenzeService();

    public List<Titolo> getTitoliConsigliati() throws Exception {

        return titoloService.getTitoliConsigliati(preferenzeService.visualizzaPreferenzeGenereUtente(),
                preferenzeService.visualizzaPreferenzePiattaformeUtente(),
                "movie");
    }

    public List<Titolo> getTitoliPopolari() throws Exception {
        return titoloService.getTitoliPiuVisti("movie");
    }

    public List<Titolo> getTitoliNovita() throws Exception {
        return titoloService.getTitoliNovita("movie");
    }

    public List<Genere> getGeneri() {
        return preferenzeService.getGeneriPerFilm();
    }
}