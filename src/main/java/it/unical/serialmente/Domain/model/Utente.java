package it.unical.serialmente.Domain.model;

import java.util.List;

public class  Utente {
    private Integer idUtente;
    private String nomeUtente;
    private CredenzialiUtente credenzialiUtente;
    private List<Genere> generiPreferiti;
    private List<Piattaforma> piattaformeScelte;
    private List<Titolo> titoliWathclist;
    private List<Titolo> titoliVisionati;
    private List<Titolo> titoliPreferiti;


    //Getter

    public Integer getIdUtente() {
        return idUtente;
    }
    public String getNomeUtente() {return nomeUtente;}
    public List<Genere> getGeneriPreferiti() {return generiPreferiti;}
    public List<Piattaforma> getPiattaformePreferite() {return piattaformeScelte;}
    public List<Titolo> getTitoliWathclist() {return titoliWathclist;}
    public List<Titolo> getTitoliVisionati() {return titoliVisionati;}
    public List<Titolo> getTitoliPreferiti() {return titoliPreferiti;}
    public CredenzialiUtente getCredenzialiUtente() {return this.credenzialiUtente;}


    //Setter
    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }
    public void setNomeUtente(String nomeUtente) {this.nomeUtente = nomeUtente;}
    public void setCredenzialiUtente(CredenzialiUtente credenzialiUtente) {this.credenzialiUtente = credenzialiUtente;}



}
