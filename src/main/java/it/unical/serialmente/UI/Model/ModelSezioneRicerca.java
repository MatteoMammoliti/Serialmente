package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.List;

public class ModelSezioneRicerca {

    private final TitoloService titoloService = new TitoloService();

    public List<Titolo> getTitoli() throws Exception {
        return titoloService.getTitoliCasuali();
    }
}