package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.ProgressoSerieDAO;
import it.unical.serialmente.Domain.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProgressoSerieDAOPostgres implements ProgressoSerieDAO {
    Connection conn;
    public ProgressoSerieDAOPostgres(Connection conn) {
        this.conn = conn;
    }


    @Override
    public boolean cambiaEpisodioCorrente(Integer idUtente,Titolo titolo, Episodio episodio) {
        String query="UPDATE progressoserie SET id_episodio=?,descrizione_episodio=?,durata_minuti_episodio=? WHERE id_serie=? AND id_utente=?";
        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1,episodio.getIdEpisodio());
            st.setString(2,episodio.getDescrizioneEpisodio());
            st.setInt(3,episodio.getDurataEpisodio());
            st.setInt(4,titolo.getIdTitolo());
            st.setInt(5, idUtente);
            return st.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cambiaStagioneCorrente(Integer idUtente,Titolo titolo, Stagione stagione) {
        String query="UPDATE progressoserie SET id_stagione=?,anno_pubblicazione_stagione=? WHERE id_serie=? AND id_utente=?";
        try(PreparedStatement rs = conn.prepareStatement(query)){
            rs.setInt(1,stagione.getIdStagione());
            rs.setInt(2,stagione.getAnnoPubblicazioneStagione());
            rs.setInt(3,titolo.getIdTitolo());
            rs.setInt(4, idUtente);
            return rs.executeUpdate()>0;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Episodio getEpisodioCorrente(Integer idUtente,Titolo titolo) {
        String query="SELECT id_episodio,descrizione_episodio,durata_minuti_episodio FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setInt(2,titolo.getIdTitolo());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Episodio episodio=new Episodio(rs.getInt("id_episodio"),rs.getInt("durata_minuti_episodio"),rs.getString("descrizione_episodio"));
                return episodio;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Stagione getStagioneCorrente(Integer idUtente,Titolo titolo) {
        String query="SELECT id_stagione,nome_stagione,anno_pubblicazione_stagione FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setInt(2,titolo.getIdTitolo());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Stagione stagione = new Stagione(rs.getString("nome_stagione"),rs.getInt("id_stagione"),rs.getInt("anno_pubblicazione_stagione"));
                return stagione;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
