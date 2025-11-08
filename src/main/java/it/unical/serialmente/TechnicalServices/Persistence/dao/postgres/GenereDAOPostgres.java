package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.TechnicalServices.Persistence.dao.GenereDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GenereDAOPostgres implements GenereDAO {
    Connection connection;
    public GenereDAOPostgres(Connection connection) {
        this.connection = connection;
    }


    @Override
    public boolean aggiungiGenere(Integer idGenere, String nomeGenere) {
        String query="INSERT INTO genere (id_genere,nome_genere) VALUES (?,?)";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, idGenere);
            st.setString(2, nomeGenere);
            return st.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminaGenere(Integer idGenere) {
        String query="DELETE FROM genere WHERE id_genere = ?";
        try(PreparedStatement st= connection.prepareStatement(query)){
            st.setInt(1, idGenere);
            return st.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Genere> restituisciGeneriPresentiDB() {
        List<Genere> generi = new ArrayList<>();
        String query="SELECT * FROM genere";
        try(PreparedStatement st = connection.prepareStatement(query)){
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Genere genere = new Genere(rs.getString("nome_genere"),rs.getInt("id_genere"));
                generi.add(genere);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return generi;
    }
}
