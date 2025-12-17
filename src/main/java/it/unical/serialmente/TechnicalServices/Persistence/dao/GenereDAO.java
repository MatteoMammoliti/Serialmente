package it.unical.serialmente.TechnicalServices.Persistence.dao;

import it.unical.serialmente.Domain.model.Genere;
import java.util.List;

public interface GenereDAO {
    List<Genere> restituisciGeneriPresentiNelDB(String tipologia);
    Integer getGenereDaNome(String nomeGenere);
}