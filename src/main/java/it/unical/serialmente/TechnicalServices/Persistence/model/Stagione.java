package it.unical.serialmente.TechnicalServices.Persistence.model;

import java.util.List;

public class Stagione {
    private String nomeStagione;
    private Integer idStagione;
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

    //Setter
    public void setNomeStagione(String nomeStagione) {
        this.nomeStagione = nomeStagione;
    }
    public void setIdStagione(Integer idStagione) {this.idStagione = idStagione;}
    public void setEpisodi(List<Episodio> episodio) {this.episodi = episodio;}
}
