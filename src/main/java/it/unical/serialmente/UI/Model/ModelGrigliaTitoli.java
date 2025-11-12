package it.unical.serialmente.UI.Model;

import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.List;

public class ModelGrigliaTitoli {

    private final TitoloService titoloService = new TitoloService();

    public List<Titolo> getTitoliPerGenere(String nome, String tipologia) throws Exception {
        return titoloService.getTitoliPerGenere(getIdGenereDaNome(nome), tipologia);
    }

    private Integer getIdGenereDaNome(String nome) {
        return titoloService.getIdGenereDaNome(nome);
    }
}