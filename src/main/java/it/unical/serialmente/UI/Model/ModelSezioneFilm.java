package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.PreferenzeService;
import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.ArrayList;
import java.util.List;

public class ModelSezioneFilm {
    private final TitoloService titoloService = new TitoloService();
    private final PreferenzeService preferenzeService = new PreferenzeService();

    public List<Titolo> getTitoliConsigliati() throws Exception {

        List<Titolo> g = titoloService.getTitoliConsigliati(preferenzeService.visualizzaPreferenzeGenereUtente(),
                preferenzeService.visualizzaPreferenzePiattaformeUtente(),
                "movie",
                1);

        g.addAll(
                titoloService.getTitoliConsigliati(preferenzeService.visualizzaPreferenzeGenereUtente(),
                        preferenzeService.visualizzaPreferenzePiattaformeUtente(),
                        "movie",
                        2)
        );
        return g;
    }

    public List<Titolo> getTitoliPopolari() throws Exception {
        List<Titolo> g = titoloService.getTitoliPiuVisti("movie", 1);
        g.addAll(titoloService.getTitoliPiuVisti("movie", 2));
        return g;
    }

    public List<Titolo> getTitoliNovita() throws Exception {
        return titoloService.getTitoliNovita("movie");
    }

    public List<Genere> getGeneri() {
        return preferenzeService.getGeneriPerFilm();
    }
}