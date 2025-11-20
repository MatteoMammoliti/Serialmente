package it.unical.serialmente.Domain.model;

import java.util.List;

public class Stagione {
    private String nomeStagione;
    private Integer idStagione;
    private Integer numeroStagioneProgressivo;
    private List<Episodio> episodi;

    public Stagione(String nomeStagione, Integer idStagione, List<Episodio> episodio) {
        this.nomeStagione = nomeStagione;
        this.idStagione = idStagione;
        this.episodi = episodio;
    }

    //Getter
    public String getNomeStagione() {
        return nomeStagione;
    }
    public Integer getIdStagione() {return idStagione;}
    public List<Episodio> getEpisodi() {return episodi;}
    public Integer getNumeroStagioneProgressivo() { return numeroStagioneProgressivo; }

    //Setter
    public void setNomeStagione(String nomeStagione) {
        this.nomeStagione = nomeStagione;
    }
    public void setIdStagione(Integer idStagione) {this.idStagione = idStagione;}
    public void setEpisodi(List<Episodio> episodio) {this.episodi = episodio;}
    public void setNumeroStagioneProgressivo(Integer numeroStagioneProgressivo) {this.numeroStagioneProgressivo = numeroStagioneProgressivo;}
}