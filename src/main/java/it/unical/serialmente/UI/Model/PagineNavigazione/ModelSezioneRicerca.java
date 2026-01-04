package it.unical.serialmente.UI.Model.PagineNavigazione;

import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;
import java.util.List;

public class ModelSezioneRicerca {

    private final TitoloService titoloService = new TitoloService();

    public List<Titolo> getTitoli() throws Exception {
        return titoloService.getTitoliCasuali();
    }

    public List<Titolo> getTitoliRicerca(String nomeTitolo, String tipologia, List<Genere> generi, Integer annoPubblicazione) throws Exception {
        return titoloService.cercaTitolo(
                nomeTitolo,
                tipologia,
                generi,
                annoPubblicazione,
                null
        );
    }
}