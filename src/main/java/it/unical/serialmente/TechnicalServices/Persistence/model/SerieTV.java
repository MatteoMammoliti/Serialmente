package it.unical.serialmente.TechnicalServices.Persistence.model;

import java.util.List;

public class SerieTV extends Titolo{
    private Integer numeroEpisodiTotali;
    private Integer numeroStagioniTotali;
    private List<Stagione> stagioni;
    private String tipologia;

    @Override
    public String getTipologia() {
        return this.tipologia;
    }

    public SerieTV(Integer idTitolo, String nomeTitolo, String trama, String immagine, double votoMedio, Integer annoPubblicazione) {
        super(idTitolo, nomeTitolo, trama, immagine, votoMedio, annoPubblicazione);
        this.tipologia = "SerieTv";
    }

    //Setter
    public void setNumeroEpisodiTotali(Integer numeroEpisodiTotali) {
        this.numeroEpisodiTotali = numeroEpisodiTotali;
    }
    public void setNumeroStagioniTotali(Integer numeroStagioniTotali) {
        this.numeroStagioniTotali = numeroStagioniTotali;
    }
    public void setStagioni(List<Stagione> stagioni) {
        this.stagioni = stagioni;
    }

    //Getter
    public Integer getNumeroEpisodiTotali() {
        return numeroEpisodiTotali;
    }
    public Integer getNumeroStagioniTotali() {
        return numeroStagioniTotali;
    }
    public List<Stagione> getStagioni() {
        return stagioni;
    }
}
