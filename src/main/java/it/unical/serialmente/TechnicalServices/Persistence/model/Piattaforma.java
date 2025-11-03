package it.unical.serialmente.TechnicalServices.Persistence.model;

public class Piattaforma {
    private String nomePiattaforma;
    private Integer idPiattaforma;

    public Piattaforma(String nomePiattaforma, Integer idPiattaforma) {this.nomePiattaforma=nomePiattaforma;this.idPiattaforma = idPiattaforma;}
    public Piattaforma(){}
    //Getter
    public String getNomePiattaforma() {
        return nomePiattaforma;
    }
    public Integer getIdPiattaforma() {return idPiattaforma;}

    //Setter
    public void setIdPiattaforma(Integer idPiattaforma) {this.idPiattaforma = idPiattaforma;}
    public void setNomePiattaforma(String nomePiattaforma) {this.nomePiattaforma = nomePiattaforma;}
}
