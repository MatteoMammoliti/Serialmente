package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.UtenteDAO;

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
    public boolean isPrimoAccesso(Integer idUtente) {
        String query = "SELECT primo_accesso FROM Utente WHERE id_utente=?";

        try(PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idUtente);
            ResultSet rs = st.executeQuery();
            if(rs.next() ) return rs.getBoolean(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void impostaPrimoAccesso(Integer idUtente) {
        String query = "UPDATE utente SET primo_accesso=? WHERE id_utente=?";

        try(PreparedStatement st = connection.prepareStatement(query)) {
            st.setBoolean(1, false);
            st.setInt(2, idUtente);
            int check = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNomeUtente(Integer idUtente) {
        String query="SELECT nome FROM utente WHERE id_utente=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, idUtente);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getString("nome");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}