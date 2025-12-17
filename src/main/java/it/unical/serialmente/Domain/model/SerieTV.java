package it.unical.serialmente.Domain.model;

import java.util.List;

public class SerieTV extends Titolo{
    private List<Stagione> stagioni;
    private final String tipologia;

    @Override
    public String getTipologia() {
        return this.tipologia;
    }

    public SerieTV(Integer idTitolo,
                   String nomeTitolo,
                   String trama,
                   String immagine,
                   double votoMedio,
                   Integer annoPubblicazione) {

        super(idTitolo, nomeTitolo, trama, immagine, votoMedio, annoPubblicazione);
        this.tipologia = "SerieTv";
    }

    public void setStagioni(List<Stagione> stagioni) {
        this.stagioni = stagioni;
    }

    public List<Stagione> getStagioni() {
        return stagioni;
    }
}