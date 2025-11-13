package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.PreferenzeService;
import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.ArrayList;
import java.util.List;

public class ModelSezioneSerieTv {
    private final TitoloService titoliService = new TitoloService();
    private final PreferenzeService preferenzeService = new PreferenzeService();

    public List<Titolo> getTitoliConsigliati() throws Exception {
        List<Titolo> g = titoliService.getTitoliConsigliati(preferenzeService.visualizzaPreferenzeGenereUtente(),
                preferenzeService.visualizzaPreferenzePiattaformeUtente(),
                "tv",
                1);

        g.addAll(titoliService.getTitoliConsigliati(preferenzeService.visualizzaPreferenzeGenereUtente(),
                preferenzeService.visualizzaPreferenzePiattaformeUtente(),
                "tv",
                2)
        );
        return g;
    }

    public List<Titolo> getTitoliPopolari() throws Exception {
        List<Titolo> g = titoliService.getTitoliPiuVisti("tv", 1);
        g.addAll(titoliService.getTitoliPiuVisti("tv", 2));
        return g;
    }

    public List<Titolo> getTitoliNovita() throws Exception {
        return titoliService.getTitoliNovita("tv");
    }

    public List<Genere> getGeneri(){
        return preferenzeService.getGeneriPerSerieTV();
    }
}