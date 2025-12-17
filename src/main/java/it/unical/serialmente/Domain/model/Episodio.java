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

    public Integer getDurataEpisodio() {return this.durataEpisodio;}
}
