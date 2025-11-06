package it.unical.serialmente.Domain.model;

public class Film extends Titolo{
    private Integer durataMinuti;
    private String tipologia;

    public Film(Integer idTitolo, String nomeTitolo, String trama, String immagine, double votoMedio, Integer durataMinuti, Integer annoPubblicazione) {
        super(idTitolo, nomeTitolo, trama, immagine, votoMedio,annoPubblicazione);
        this.durataMinuti = durataMinuti;
        this.tipologia="Film";
    }

    //Getter
    public Integer getDurataMinuti() {
        return durataMinuti;
    }

    //Setter
    public void setDurataMinuti(Integer durataMinuti) {this.durataMinuti = durataMinuti;}

    @Override
    public String getTipologia() {
        return this.tipologia;
    }
}
