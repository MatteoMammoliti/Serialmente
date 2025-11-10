package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.PreferenzeService;
import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.ArrayList;
import java.util.List;

public class ModelHomepage {
    private TitoloService titoloService = new TitoloService();
    private PreferenzeService preferenzeService = new PreferenzeService();

    public List<Titolo> getTitoliConsigliati() throws Exception {
        List<Genere> generi = new ArrayList<>();
        List<Piattaforma> piattaforma = new ArrayList<>();
        Genere genere = new Genere("Azione",28);
        Piattaforma pia = new Piattaforma("Netflix",8);
        generi.add(genere);
        piattaforma.add(pia);

        return titoloService.getTitoliConsigliati(generi,piattaforma, "movie");
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