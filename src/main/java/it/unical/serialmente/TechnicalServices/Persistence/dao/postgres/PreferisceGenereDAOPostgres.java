package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.PreferisceGenereDAO;
import it.unical.serialmente.Domain.model.Genere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PreferisceGenereDAOPostgres implements PreferisceGenereDAO {
    Connection connection;
    public PreferisceGenereDAOPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Genere> getGeneriPreferitiUtente(Integer idUtente) {
        List<Genere> generiPreferitiUtente = new ArrayList<>();
        String query="SELECT * FROM preferiscegenere p JOIN genere g on p.id_genere=g.id_genere WHERE p.id_utente=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Genere genere = new Genere(rs.getString("nome_genere"),rs.getInt("id_genere"));
                generiPreferitiUtente.add(genere);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  generiPreferitiUtente;
    }

    @Override
    public boolean aggiungiGenerePreferitoUtente(Integer idUtente, Integer idGenere) {
        String query="INSERT INTO preferiscegenere (id_utente,id_genere) VALUES (?,?)";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setInt(2,idGenere);
            return st.executeUpdate()>0;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviGenerePreferitoUtente(Integer idUtente, Integer idGenere) {
        String query="DELETE FROM preferiscegenere WHERE id_utente=? AND id_genere=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setInt(2,idGenere);
            return  st.executeUpdate()>0;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
