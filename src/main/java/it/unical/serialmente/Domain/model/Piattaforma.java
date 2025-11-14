package it.unical.serialmente.Domain.model;

public class Piattaforma {
    private String nomePiattaforma;
    private Integer idPiattaforma;
    private String imgUrl;

    public Piattaforma(String nomePiattaforma, Integer idPiattaforma) {this.nomePiattaforma=nomePiattaforma;this.idPiattaforma = idPiattaforma;}
    public Piattaforma(){}
    //Getter
    public String getNomePiattaforma() {
        return nomePiattaforma;
    }
    public Integer getIdPiattaforma() {return idPiattaforma;}
    public String getImgUrl() {return imgUrl;}

    //Setter
    public void setIdPiattaforma(Integer idPiattaforma) {this.idPiattaforma = idPiattaforma;}
    public void setNomePiattaforma(String nomePiattaforma) {this.nomePiattaforma = nomePiattaforma;}
    public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}
}
