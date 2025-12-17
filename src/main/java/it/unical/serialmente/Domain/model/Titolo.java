package it.unical.serialmente.Domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class  Titolo {
    private Integer idTitolo;
    private String nomeTitolo;
    private String trama;
    private String immagine;
    private double votoMedio;
    private Integer annoPubblicazione;
    private List<Genere> generiPresenti;
    private List<Piattaforma> piattaforme;

    public Titolo() {}
    public Titolo(Integer idTitolo,
                  String nomeTitolo,
                  String trama,
                  String immagine,
                  double votoMedio,
                  Integer annoPubblicazione) {

        this.idTitolo = idTitolo;
        this.nomeTitolo = nomeTitolo;
        this.trama = trama;
        this.immagine = immagine;
        this.votoMedio = votoMedio;
        this.annoPubblicazione = annoPubblicazione;
        this.generiPresenti = new ArrayList<>();
        this.piattaforme = new ArrayList<>();
    }


    public abstract String getTipologia();
    public Integer getIdTitolo() {
        return idTitolo;
    }
    public String getNomeTitolo() {
        return nomeTitolo;
    }
    public String getTrama() {
        return trama;
    }
    public String getImmagine() {
        return immagine;
    }
    public double getVotoMedio() {
        return votoMedio;
    }
    public Integer getAnnoPubblicazione(){return this.annoPubblicazione;}
    public List<Genere> getGeneriPresenti() {return this.generiPresenti;}
    public List<Piattaforma> getPiattaforme() {return this.piattaforme;}

    public void setIdTitolo(Integer idTitolo) {
        this.idTitolo = idTitolo;
    }
    public void setNomeTitolo(String nomeTitolo) {
        this.nomeTitolo = nomeTitolo;
    }
    public void aggiungiGenere(Genere genere) {
        this.generiPresenti.add(genere);
    }
    public void aggiungiPiattaforme(Piattaforma piattaforma) {this.piattaforme.add(piattaforma);}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Titolo titolo = (Titolo) o;
        return Double.compare(votoMedio, titolo.votoMedio) == 0 && Objects.equals(idTitolo, titolo.idTitolo) && Objects.equals(nomeTitolo, titolo.nomeTitolo) && Objects.equals(trama, titolo.trama) && Objects.equals(immagine, titolo.immagine) && Objects.equals(annoPubblicazione, titolo.annoPubblicazione) && Objects.equals(generiPresenti, titolo.generiPresenti) && Objects.equals(piattaforme, titolo.piattaforme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTitolo, nomeTitolo, trama, immagine, votoMedio, annoPubblicazione, generiPresenti, piattaforme);
    }
}