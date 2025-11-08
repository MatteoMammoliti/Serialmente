package it.unical.serialmente.Domain.model;

import java.util.List;

public class Stagione {
    private String nomeStagione;
    private Integer idStagione;
    private Integer annoPubblicazioneStagione;
    private Integer numeroStagioneProgressivo;
    private List<Episodio> episodi;
    private boolean completata;

    public Stagione(String nomeStagione, Integer idStagione, List<Episodio> episodio) {
        this.nomeStagione = nomeStagione;
        this.idStagione = idStagione;
        this.episodi = episodio;
        this.completata = false;
    }

    //Getter
    public String getNomeStagione() {
        return nomeStagione;
    }
    public Integer getIdStagione() {return idStagione;}
    public List<Episodio> getEpisodi() {return episodi;}
    public Integer  getAnnoPubblicazioneStagione() {return annoPubblicazioneStagione;}
    public Integer getNumeroStagioneProgressivo() { return numeroStagioneProgressivo; }
    public boolean isCompletata() {return completata;
    }

    //Setter
    public void setNomeStagione(String nomeStagione) {
        this.nomeStagione = nomeStagione;
    }
    public void setIdStagione(Integer idStagione) {this.idStagione = idStagione;}
    public void setEpisodi(List<Episodio> episodio) {this.episodi = episodio;}
    public void setAnnoPubblicazioneStagione(Integer annoPubblicazioneStagione) {this.annoPubblicazioneStagione=annoPubblicazioneStagione;}
    public void setNumeroStagioneProgressivo(Integer numeroStagioneProgressivo) {this.numeroStagioneProgressivo = numeroStagioneProgressivo;}
    public void setCompletata(boolean completata) {this.completata = completata;}
}
