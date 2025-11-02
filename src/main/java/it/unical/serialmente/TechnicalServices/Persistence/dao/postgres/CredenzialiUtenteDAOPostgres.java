package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.CredenzialiUtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.model.CredenzialiUtente;
import it.unical.serialmente.TechnicalServices.Persistence.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CredenzialiUtenteDAOPostgres implements CredenzialiUtenteDAO {
    private Connection connection;

    public CredenzialiUtenteDAOPostgres(Connection connection) {
        this.connection = connection;
    }

    /**
     * Crea una nuova instanza in credenzialiutente.Verranno memorizzati i dati di accesso/recupero password
     * per l'utente in questione.
     * @param credenziali
     * @param idUtente
     * @return valore booleano per confermare o meno se l'operazione è avvenuta con successo.
     */
    @Override
    public boolean insertCredenzialiUtente(CredenzialiUtente credenziali,Integer idUtente) {
        String query ="INSERT INTO credenzialiutente (id_utente,email,password,domanda_sicurezza,risposta_domanda_sicurezza) Values(?,?,?,?,?)";
        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setString(2,credenziali.getEmail());
            st.setString(3,credenziali.getPassword());
            st.setString(4,credenziali.getDomandaSicurezza());
            st.setString(5,credenziali.getRispostaDomandaSicurezza());

            int riga=st.executeUpdate();
            if(riga>0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CredenzialiUtente getCredenzialiUtente(Utente utente) {
        String query="SELECT * FROM credenzialiutente WHERE id_utente=?";
        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setInt(1,utente.getIdUtente());
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                CredenzialiUtente credenziali=new CredenzialiUtente();
                credenziali.setEmail(rs.getString("email"));
                credenziali.setPassword(rs.getString("password"));
                credenziali.setDomandaSicurezza(rs.getString("domanda_sicurezza"));
                credenziali.setRispostaDomandaSicurezza(rs.getString("risposta_domanda_sicurezza"));
                return credenziali;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteCredenzialiUtente(Integer idUtente) {
        String query="DELETE FROM credenzialiutente WHERE id_utente=?";
        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            int riga=st.executeUpdate();
            if(riga>0){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cercaEmail(String email) {
        String query="SELECT * FROM credenzialiutente WHERE email=?";
        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setString(1,email);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();

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
        String query="SELECT * FROM credenzialiutente WHERE email=? AND password=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, email);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if(rs.next()){

                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Permette di risalire all'id dell'utente a partire dalla propria Email.
     * @param email
     * @return L'id dell'utente con l'email data in input.
     */
    @Override
    public Integer cercaIdUtente(String email) {
        String query="SELECT * FROM credenzialiutente WHERE email=?";
        try(PreparedStatement st=connection.prepareStatement(query)){
            st.setString(1,email);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt("id_utente");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
