package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.UtenteDAO;
import it.unical.serialmente.Domain.model.Utente;

import java.sql.*;

public class UtenteDAOPostgres implements UtenteDAO {
    Connection connection;

    public UtenteDAOPostgres(Connection connection) {
        this.connection = connection;
    }


    /**
     * Crea un'istanza di Utente.Saranno salvati l'id utente e il nome dell'utente.
     *
     * @param "nome utente"
     * @return valore booleano per confermare o meno il successo dell'operazione.
     */
    @Override
    public int inserisciUtente(String nome) {
        String query="INSERT INTO utente (nome) VALUES (?)";
        try(PreparedStatement st=connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)){
            st.setString(1,nome);
            int righe=st.executeUpdate();
            if(righe>0){
                ResultSet rs=st.getGeneratedKeys();
                if(rs.next()){
                    return rs.getInt(1);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();

        }
        return 0;
    }

    @Override
    public boolean cancellaUtente(Integer idUtente) {
        String query="DELETE FROM Utente WHERE id_utente=?";
        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            int righe=st.executeUpdate();
            if(righe>0){
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean modificaNomeUtente(Integer idUtente, String nuovoNome) {
        String query="UPDATE utente Set nome=? WHERE id_utente=?";
        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setString(1,nuovoNome);
            st.setInt(2,idUtente);
            int righe=st.executeUpdate();
            if(righe>0){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
