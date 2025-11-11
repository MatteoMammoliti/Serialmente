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
        List<Genere> generi = new ArrayList<>();
        List<Piattaforma> piattaforma = new ArrayList<>();
        Genere genere = new Genere("Kids",10762);
        Piattaforma pia = new Piattaforma("Netflix",8);
        generi.add(genere);
        piattaforma.add(pia);

        return titoliService.getTitoliConsigliati(generi,piattaforma, "tv");
    }

    public List<Titolo> getTitoliPopolari() throws Exception {
        return titoliService.getTitoliPiuVisti("tv");
    }

    public List<Titolo> getTitoliNovita() throws Exception {
        return titoliService.getTitoliNovita("tv");
    }

    public List<Genere> getGeneri(){
        return preferenzeService.getGeneriPerSerieTV();
    }
}
