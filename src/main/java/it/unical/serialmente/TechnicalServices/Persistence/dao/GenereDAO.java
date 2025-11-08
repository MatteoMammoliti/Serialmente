package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Genere;

import java.util.List;

public interface GenereDAO {
    boolean aggiungiGenere(Integer idGenere,String nomeGenere);
    boolean eliminaGenere(Integer idGenere);
    List<Genere> restituisciGeneriPresentiDB();

}
