package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.TechnicalServices.Persistence.dao.PiattaformaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PiattaformaDAOPostgres implements PiattaformaDAO {
    Connection connection;
    public PiattaformaDAOPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Piattaforma> getListaPiattaforma() {
        List<Piattaforma> piattaforma = new ArrayList<>();
        String query="SELECT * FROM piattaforma";

        try(PreparedStatement st = connection.prepareStatement(query)){

            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Piattaforma p = new Piattaforma(rs.getString("nome_piattaforma"),rs.getInt("id_piattaforma"));
                piattaforma.add(p);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return piattaforma;
    }
}