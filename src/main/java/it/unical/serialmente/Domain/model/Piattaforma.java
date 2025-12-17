package it.unical.serialmente.Domain.model;

import java.util.Objects;

public class Piattaforma {
    private String nomePiattaforma;
    private Integer idPiattaforma;

    public Piattaforma(String nomePiattaforma, Integer idPiattaforma) {
        this.nomePiattaforma=nomePiattaforma;
        this.idPiattaforma = idPiattaforma;
    }

    public Piattaforma(){}

    public String getNomePiattaforma() {
        return nomePiattaforma;
    }
    public Integer getIdPiattaforma() {return idPiattaforma;}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Piattaforma that = (Piattaforma) o;
        return Objects.equals(nomePiattaforma, that.nomePiattaforma) && Objects.equals(idPiattaforma, that.idPiattaforma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomePiattaforma, idPiattaforma);
    }
}