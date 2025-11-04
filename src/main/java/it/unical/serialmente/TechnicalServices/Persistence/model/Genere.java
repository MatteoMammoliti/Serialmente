package it.unical.serialmente.TechnicalServices.Persistence.model;

import java.util.Objects;

public class Genere {
    private String nomeGenere;
    private Integer idGenere;

    public Genere(){}
    public Genere(String nomeGenere, Integer idGenere) {this.nomeGenere = nomeGenere;this.idGenere = idGenere;}

    //Setter
    public void setNomeGenere(String nomeGenere) {this.nomeGenere = nomeGenere;}
    public void setIdGenere(Integer idGenere) {this.idGenere = idGenere;}

    //Getter
    public String getNomeGenere() {return this.nomeGenere;}
    public Integer getIdGenere() {return this.idGenere;}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Genere genere = (Genere) o;
        return Objects.equals(nomeGenere, genere.nomeGenere) && Objects.equals(idGenere, genere.idGenere);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeGenere, idGenere);
    }
}
