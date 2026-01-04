package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.CredenzialiUtenteDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class CredenzialiUtenteDAOPostgres implements CredenzialiUtenteDAO {
    private Connection connection;

    public CredenzialiUtenteDAOPostgres(Connection connection) {
        this.connection = connection;
    }

    /**
     * Crea una nuova instanza in credenzialiutente.Verranno memorizzati i dati di accesso/recupero password
     * per l'utente in questione.
     * @param "Email,password,domanda sicurezza e risposta sicurezza"
     * @param idUtente
     * @return valore booleano per confermare o meno se l'operazione è avvenuta con successo.
     */
    @Override
    public boolean insertCredenzialiUtente(String email,
                                           String password,
                                           String domandaSicurezza,
                                           String rispostaDomandaSicurezza,
                                           Integer idUtente) {

        String query ="INSERT INTO credenzialiutente (id_utente,email,password,domanda_sicurezza,risposta_domanda_sicurezza) values(?,?,?,?,?)";

        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setString(2,email);
            st.setString(3,password);
            st.setString(4,domandaSicurezza);
            st.setString(5,rispostaDomandaSicurezza);

            int riga=st.executeUpdate();
            if(riga>0){
                return true;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean cercaEmail(String email) {
        String query="SELECT * FROM credenzialiutente WHERE email=?";

        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setString(1,email);
            ResultSet rs=st.executeQuery();

            if(rs.next()) return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Permette di verificare se le credenziali inserite dall'utente risultano presenti all'interno del DB e, quindi, corrette.
     * @param email
     * @param password
     * @return
     */
    @Override
    public boolean validaCredenzialiUtente(String email,String password) {
        String query="SELECT * FROM credenzialiutente WHERE email=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                String passcript = rs.getString("password");
                return BCrypt.checkpw(password, passcript);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Permette di risalire all'id dell'utente a partire dalla propria Email.
     * @return L'id dell'utente con l'email data in input.
     */
    @Override
    public Integer cercaIdUtente(String email) {
        String query="SELECT * FROM credenzialiutente WHERE email=?";

        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setString(1,email);
            ResultSet rs = st.executeQuery();
            if(rs.next()) return rs.getInt("id_utente");

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getDomandaSicurezza(String email) {
        String query="SELECT domanda_sicurezza FROM credenzialiutente WHERE email=?";

        try(PreparedStatement st = connection.prepareStatement(query)){

            st.setString(1,email);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return rs.getString("domanda_sicurezza");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean confrontoRispostaSicurezza(String email, String risposta) {
        String query="SELECT risposta_domanda_sicurezza FROM credenzialiutente WHERE email=?";

        try(PreparedStatement st = connection.prepareStatement(query)){

            st.setString(1,email);
            ResultSet rs = st.executeQuery();

            if(rs.next()) return BCrypt.checkpw(risposta, rs.getString("risposta_domanda_sicurezza"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cambiaPassword(String email, String password) {
        String query= "UPDATE credenzialiutente SET password=? WHERE email=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1,password);
            st.setString(2,email);
            return st.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}