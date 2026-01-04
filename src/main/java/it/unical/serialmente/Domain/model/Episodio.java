package it.unical.serialmente.Domain.model;

public class Episodio {

    private Integer idEpisodio;
    private Integer durataEpisodio;


    public Episodio(Integer idEpisodio, Integer durataEpisodio) {
        this.idEpisodio = idEpisodio;
        this.durataEpisodio = durataEpisodio;
    }

    public Integer getDurataEpisodio() {return this.durataEpisodio;}
}
