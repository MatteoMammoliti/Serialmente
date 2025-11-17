package it.unical.serialmente.UI.Model.PagineNavigazione;

import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Titolo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModelGrigliaTitoli {

    private final TitoloService titoloService = new TitoloService();

    public CompletableFuture<List<Titolo>> getTitoliPerGenere(String nome, String tipologia, Integer pagina) throws Exception {
        return titoloService.getTitoliPerGenerePaginaSingola(
                titoloService.getIdGenereDaNome(nome),
                tipologia,
                pagina
        );
    }
}