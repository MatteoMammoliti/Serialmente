package it.unical.serialmente.UI.Model.ModelAutenticazione;

import it.unical.serialmente.Application.Service.PreferenzeService;
import it.unical.serialmente.Application.Service.UtenteService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.Utente;

import java.util.List;

public class ModelPaginaPreferenze {

    private final PreferenzeService preferenzeService = new PreferenzeService();
    private final UtenteService utenteService = new UtenteService();

    public List<Genere> getGeneri() {
        List<Genere> g1 =  this.preferenzeService.getGeneriPerFilm();
        List<Genere> g2 =  this.preferenzeService.getGeneriPerSerieTV();
        g1.addAll(g2);
        return g1;
    }

    public List<Piattaforma> getPiattaforme() {
        return this.preferenzeService.getPiattaformeDisponibili();
    }

    public void aggiornaPreferenzeGeneri(List<Genere> g) {
        preferenzeService.aggiornaPreferenzeGenere(
                g,
                "AGGIUNTA"
        );
    }

    public void aggiornaPreferenzePiattaforme(List<Piattaforma> p) {
        preferenzeService.aggiornaPreferenzePiattaforme(
                p,
                "AGGIUNTA"
        );
    }

    public void impostaPrimoAccesso(Utente u) {
        utenteService.impostaPrimoAccesso(u);
    }
}