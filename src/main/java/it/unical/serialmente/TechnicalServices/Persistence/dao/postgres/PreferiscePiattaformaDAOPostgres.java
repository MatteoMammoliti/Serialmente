package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.PreferiscePiattaformaDAO;
import it.unical.serialmente.Domain.model.Piattaforma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PreferiscePiattaformaDAOPostgres implements PreferiscePiattaformaDAO {
    Connection conn;
    public PreferiscePiattaformaDAOPostgres(Connection conn) {
        this.conn = conn;
    }
    @Override
    public List<Piattaforma> getPiattaformePreferiteUtente(Integer idUtente) {
        List<Piattaforma> piattaformePreferiteUtente = new ArrayList<>();
        String query="SELECT * FROM preferiscepiattaforma p JOIN piattaforma pi ON p.id_piattaforma=pi.id_piattaforma WHERE p.id_utente=?";
        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1,idUtente);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Piattaforma piattaforma= new Piattaforma(rs.getString("nome_piattaforma"),rs.getInt("id_piattaforma"));
                piattaformePreferiteUtente.add(piattaforma);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return piattaformePreferiteUtente;
    }

    @Override
    public boolean aggiungiPiattaformaPreferitaUtente(Integer idUtente, Integer idPiattaforma) {
        String query="INSERT INTO preferiscepiattaforma (id_utente,id_piattaforma) VALUES (?,?)";
        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setInt(2,idPiattaforma);
            return st.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rimuoviPiattaformaPreferitaUtente(Integer idUtente, Integer idPiattaforma) {
        String query="DELETE FROM preferiscepiattaforma WHERE id_utente=? AND id_piattaforma=?";
        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setInt(2,idPiattaforma);
            return st.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
