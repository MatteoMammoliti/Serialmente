package it.unical.serialmente.UI.Model.PagineNavigazione;

import it.unical.serialmente.Application.Service.PreferenzeService;
import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ModelSezioneSerieTv {
    private final TitoloService titoliService = new TitoloService();
    private final PreferenzeService preferenzeService = new PreferenzeService();

    public List<Titolo> getTitoliConsigliati() throws Exception {

        List<Genere> generiUtente = preferenzeService.visualizzaPreferenzeGenereUtente();
        List<Piattaforma> piattaformeUtente = preferenzeService.visualizzaPreferenzePiattaformeUtente();

        Set<Titolo> result = new LinkedHashSet<>();

        result.addAll(titoliService.getTitoliConsigliati(
                generiUtente,
                piattaformeUtente,
                "tv",
                1
        ));

        result.addAll(titoliService.getTitoliConsigliati(
                generiUtente,
                piattaformeUtente,
                "tv",
                2
        ));

        result.addAll(titoliService.getTitoliConsigliati(
                getGeneriStorico(),
                piattaformeUtente,
                "tv",
                2
        ));

        return new ArrayList<>(result);
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

    public List<Genere> getGeneriStorico() {
        return preferenzeService.getGeneriStoricosSerieTV();
    }
}