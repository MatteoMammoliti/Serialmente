package it.unical.serialmente.Domain.model;

public class Episodio {
    private Integer idEpisodio;
    private Integer durataEpisodio;
    private String descrizioneEpisodio;
    private boolean visualizzato;

    public Episodio(Integer idEpisodio, Integer durataEpisodio,String descrizione) {
        this.idEpisodio = idEpisodio;
        this.durataEpisodio = durataEpisodio;
        this.descrizioneEpisodio = descrizione;
        this.visualizzato = false;
    }

    //Setter
    public void setDescrizioneEpisodio(String descrizioneEpisodio) {this.descrizioneEpisodio = descrizioneEpisodio;}
    public void setIdEpisodio(Integer idEpisodio) {this.idEpisodio = idEpisodio;}
    public void setDurataEpisodio(Integer durataEpisodio ) {this.durataEpisodio = durataEpisodio;}
    public void setVisualizzato(boolean visualizzato) { this.visualizzato = visualizzato;}

    //Getter
    public Integer getDurataEpisodio() {return this.durataEpisodio;}
    public Integer getIdEpisodio() {return this.idEpisodio;}
    public String getDescrizioneEpisodio() {return this.descrizioneEpisodio;}
    public boolean isVisualizzato() { return visualizzato;}
}
