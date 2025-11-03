package it.unical.serialmente.TechnicalServices.Persistence.model;

public class Episodio {
    private Integer idEpisodio;
    private Integer durataEpisodio;

    public Episodio(Integer idEpisodio, Integer durataEpisodio) {
        this.idEpisodio = idEpisodio;
        this.durataEpisodio = durataEpisodio;
    }

    //Setter
    public void setIdEpisodio(Integer idEpisodio) {this.idEpisodio = idEpisodio;}
    public void setDurataEpisodio(Integer durataEpisodio ) {this.durataEpisodio = durataEpisodio;}

    //Getter
    public Integer getDurataEpisodio() {return this.durataEpisodio;}
    public Integer getIdEpisodio() {return this.idEpisodio;}
}
