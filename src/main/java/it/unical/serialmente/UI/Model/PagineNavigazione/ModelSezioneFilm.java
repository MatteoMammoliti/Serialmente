package it.unical.serialmente.UI.Model.PagineNavigazione;

import it.unical.serialmente.Application.Service.PreferenzeService;
import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.*;

public class ModelSezioneFilm {
    private final TitoloService titoloService = new TitoloService();
    private final PreferenzeService preferenzeService = new PreferenzeService();

    public List<Titolo> getTitoliConsigliati() throws Exception {
        Set<Titolo> result = new LinkedHashSet<>();

        List<Genere> generiUtente = preferenzeService.visualizzaPreferenzeGenereUtente();
        List<Piattaforma> piattaformeUtente = preferenzeService.visualizzaPreferenzePiattaformeUtente();

        result.addAll(
                titoloService.getTitoliConsigliati(
                        generiUtente,
                        piattaformeUtente,
                        "movie", 1
                )
        );

        result.addAll(
                titoloService.getTitoliConsigliati(
                        generiUtente,
                        piattaformeUtente,
                        "movie", 2
                )
        );

        result.addAll(
                titoloService.getTitoliConsigliati(
                        getGeneriStorico(),
                        piattaformeUtente,
                        "movie", 1
                )
        );

        return new ArrayList<>(result);
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

    private List<Genere> getGeneriStorico() {
        return preferenzeService.getGeneriStoricoFilm();
    }
}