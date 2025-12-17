package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.TechnicalServices.Persistence.dao.GenereDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenereDAOPostgres implements GenereDAO {
    Connection connection;
    public GenereDAOPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Genere> restituisciGeneriPresentiNelDB(String tipologia) {
        List<Genere> generi = new ArrayList<>();
        String query="SELECT * FROM genere WHERE tipologia = ?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, tipologia);
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

    @Override
    public Integer getGenereDaNome(String nomeGenere) {
        String query = "SELECT id_genere FROM genere WHERE nome_genere = ?";
        try(PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, nomeGenere);
            ResultSet rs = st.executeQuery();
            if(rs.next()) return rs.getInt("id_genere");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}