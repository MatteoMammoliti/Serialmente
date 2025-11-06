package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Episodio;
import it.unical.serialmente.Domain.model.Stagione;
import it.unical.serialmente.Domain.model.Titolo;

public interface ProgressoSerieDAO {
    boolean cambiaEpisodioCorrente(Integer idUtente,Titolo titolo, Episodio episodio);
    boolean cambiaStagioneCorrente(Integer idUtente,Titolo titolo, Stagione stagione);
    Episodio getEpisodioCorrente(Integer idUtente,Titolo titolo);
    Stagione getStagioneCorrente(Integer idUtente,Titolo titolo);
}
