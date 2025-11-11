package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.SelezioneTitoloDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.TitoloDAO;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SelezioneTitoloDAOPostgres implements SelezioneTitoloDAO {
    Connection connection;

    public SelezioneTitoloDAOPostgres(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Titolo> restituisciTitoliInLista(Integer idUtente,String nomeLista) {
        List<Titolo> lista = new ArrayList<>();
        if(!nomeLista.equals("Watchlist") && !nomeLista.equals("Visionati") && !nomeLista.equals("Preferiti")){
            System.out.println("Nome lista non valido");
            return lista;
        }
        TitoloDAO titoloDao= DBManager.getInstance().getTitoloDAO();
        String query;
        if(nomeLista.equals("Preferiti")){
            query="SELECT * FROM selezionetitolo WHERE id_utente=? AND tipo_lista=? AND e_preferito=true";
            nomeLista="Visionati";
        }else{
            query="SELECT * FROM selezionetitolo WHERE id_utente=? AND tipo_lista=? AND e_preferito=false";
        }
        try(PreparedStatement st =connection.prepareStatement(query)){
            st.setInt(1, idUtente);
            st.setString(2,nomeLista);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Titolo titolo = titoloDao.restituisciTitoloPerId(rs.getInt("id_titolo"));
                lista.add(titolo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Permette di aggiungere un titolo presente nel Db interno in una delle tre liste dell'utente.
     * Nel caso sia Watchlist crea l'istanza, se Visionati aggiorna il valore di tipo_lista,
     * se preferiti aggiorna il valore di e_preferito.
     * @param idTitolo
     * @param nomeLista parametri accettati: Watchlist,Visionati,Preferiti.
     * @return valore booleano per confermare l'eventuale successo del metodo.
     */
    @Override
    public boolean aggiungiTitoloInLista(Integer idUtente,Integer idTitolo, String nomeLista) {
        if(!nomeLista.equals("Watchlist") && !nomeLista.equals("Visionati") && !nomeLista.equals("Preferiti")){
            System.out.println("Nome lista non valido");
            return false;
        }
        String query;
        if(nomeLista.equals("Preferiti")){
            query="UPDATE selezionetitolo SET e_preferito=true WHERE id_utente=? AND id_titolo=?";
        }
        else if(nomeLista.equals("Visionati")){
            query="UPDATE selezionetitolo SET tipo_lista='Visionati' WHERE id_utente=? AND id_titolo=?";
        }
        else{
            query ="INSERT INTO selezionetitolo (id_utente,id_titolo,tipo_lista) VALUES (?,?,?)";
        }
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setInt(2,idTitolo);
            if(nomeLista.equals("Watchlist")){
                st.setString(3,nomeLista);
            }
            int riga= st.executeUpdate();
            if(riga>0){
                System.out.println("Titolo aggiunto nella lista "+nomeLista);
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Titolo non aggiunto "+nomeLista);
        return false;
    }


    /**
     * Permette di rimuovere un titolo da una delle tre liste dell'utente.
     * Nel caso sia Watchlist o Visionati rimuove l'istanza, altrimenti modifica il valore di
     * e_preferito.
     * @param idTitolo
     * @param nomeLista Parametri accettati: Watchlist,Visionati,Preferiti.
     * @return valore booleano per confermare l'eventuale successo del metodo.
     */
    @Override
    public boolean eliminaTitoloInLista(Integer idUtente,Integer idTitolo, String nomeLista) {
        if(!nomeLista.equals("Watchlist") && !nomeLista.equals("Visionati") && !nomeLista.equals("Preferiti")){
            System.out.println("Nome lista non valido");
            return false;
        }
        if(nomeLista.equals("Preferiti")){
            String query="UPDATE selezionetitolo SET e_preferito=false WHERE id_titolo=? AND id_utente=?";
            try(PreparedStatement st = connection.prepareStatement(query)){
                st.setInt(1,idTitolo);
                st.setInt(2,idUtente);
                int riga= st.executeUpdate();
                if(riga>0){
                    System.out.println("Titolo" +idTitolo+" tolto da "+nomeLista);
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            String query= "DELETE FROM selezionetitolo WHERE id_titolo=? AND id_utente=?";
            try(PreparedStatement st=connection.prepareStatement(query)){
                st.setInt(1,idTitolo);
                st.setInt(2,idUtente);
                int riga= st.executeUpdate();
                if(riga>0){
                    System.out.println("Titolo" +idTitolo+" tolto "+nomeLista);
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Ritorna,senza duplicati, i generi dei film presenti nelle liste dell'utente, serviranno
     * per decretare i titoli consigliati da mostrare nella pagina iniziale dell'applicazione.
     * @return
     */
    @Override
    public HashSet<Genere> restituisciGeneriVisionati(Integer idUtente) {
        TitoloDAO titoloDao=DBManager.getInstance().getTitoloDAO();
        HashSet<Genere> generi=new HashSet<>();
        String query="SELECT * FROM selezionetitolo WHERE id_utente=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idUtente);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                List<Genere> generiTitolo = new ArrayList<>();
                generiTitolo=titoloDao.restituisciGeneriTitolo(rs.getInt("id_titolo"));
                for(Genere genere:generiTitolo){
                    generi.add(genere);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return generi;

    }

    public boolean controlloTitoloInListe(Integer idTitolo) {
        String query = "SELECT 1 FROM selezionetitolo WHERE id_titolo = ?";

        try(PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idTitolo);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Integer getNumeroFilmVisionati(Integer idUtente) {
        String query = "SELECT COUNT (*) FROM selezionetitolo s JOIN titolo t on " +
                "s.id_titolo=t.id_titolo WHERE s.id_utente=? and s.tipo_lista=? AND t.tipologia=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, idUtente);
            st.setString(2,"Visionati");
            st.setString(3,"Film");
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Integer getMinutiVisioneFilm(Integer idUtente) {
        String query= "SELECT SUM(t.durata_minuti) AS totale_minuti FROM selezionetitolo s JOIN titolo t" +
                " on s.id_titolo=t.id_titolo WHERE s.id_utente=? AND t.tipologia=? AND s.tipo_lista=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, idUtente);
            st.setString(2,"Film");
            st.setString(3,"Visionati");
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
