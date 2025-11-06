package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.TitoloDAO;
import it.unical.serialmente.Domain.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitoloDAOPostgres implements TitoloDAO {

    Connection con;

    public TitoloDAOPostgres(Connection con) {
        this.con = con;
    }

    @Override
    public Titolo restituisciTitoloPerId(Integer idTitolo) {
        Titolo titolo=null;
        List<Integer> genereAggiunti = new ArrayList<>();
        List<Integer> piattaformeAggiunte = new ArrayList<>();
        String query="SELECT * FROM titolo t LEFT JOIN trasmessosu tr on t.id_titolo=tr.id_titolo LEFT JOIN appartiene a on a.id_titolo=t.id_titolo" +
                " LEFT JOIN genere g on g.id_genere=a.id_genere LEFT JOIN piattaforma p on p.id_piattaforma=tr.id_piattaforma  WHERE t.id_titolo=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idTitolo);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                if(titolo==null){
                    if(rs.getString("tipologia").equals("SerieTv")){
                        titolo=new SerieTV(rs.getInt("id_titolo"),rs.getString("nome_titolo"),
                                rs.getString("trama"), rs.getString("immagine"),
                                rs.getDouble("voto_medio"),rs.getInt("anno_pubblicazione"));
                    }
                    else {
                        titolo=new Film(rs.getInt("id_titolo"),rs.getString("nome_titolo"),
                                rs.getString("trama"), rs.getString("immagine"),
                                rs.getDouble("voto_medio"),rs.getInt("durata_minuti"),
                                rs.getInt("anno_pubblicazione"));
                    }
                }
                int idGenere=rs.getInt("id_genere");
                boolean genereNull=rs.wasNull();
                String nomeGenere=rs.getString("nome_genere");
                if(!genereNull && !genereAggiunti.contains(idGenere)){
                    Genere genere=new Genere(nomeGenere,idGenere);
                    titolo.aggiungiGenere(genere);
                    genereAggiunti.add(idGenere);
                }
                int idPiattaforma=rs.getInt("id_piattaforma");
                boolean piattaformaNull=rs.wasNull();
                String nomePiattaforma=rs.getString("nome_piattaforma");
                if(!piattaformaNull && !piattaformeAggiunte.contains(idPiattaforma)){
                    Piattaforma piattaforma = new Piattaforma(nomePiattaforma,idPiattaforma);
                    titolo.aggiungiPiattaforme(piattaforma);
                    piattaformeAggiunte.add(idPiattaforma);
                }
            }
            return titolo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean aggiungiTitolo(Titolo titolo) throws SQLException {
        String queryInserimentoTitolo="INSERT into titolo (id_titolo,nome_titolo,tipologia,trama,immagine,voto_medio," +
                "durata_minuti,anno_pubblicazione) " + "VALUES(?,?,?,?,?,?,?,?)";
        String queryInserimentoGeneri="INSERT into appartiene (id_titolo,id_genere) VALUES(?,?) ";
        String queryInserimentoPiattaforme="INSERT INTO trasmessosu (id_titolo,id_piattaforma) VALUES(?,?)";

        con.setAutoCommit(false);
        try(PreparedStatement stTitolo=con.prepareStatement(queryInserimentoTitolo);
            PreparedStatement stGeneri=con.prepareStatement(queryInserimentoGeneri);
            PreparedStatement stPiattafome=con.prepareStatement(queryInserimentoPiattaforme)){
            stTitolo.setInt(1,titolo.getIdTitolo());
            stTitolo.setString(2,titolo.getNomeTitolo());
            stTitolo.setString(3,titolo.getTipologia());
            stTitolo.setString(4,titolo.getTrama());
            stTitolo.setString(5,titolo.getImmagine());
            stTitolo.setDouble(6,titolo.getVotoMedio());
            if(titolo.getTipologia().equals("SerieTv")){
                stTitolo.setDouble(7,0);
            }
            else {
                Film titoloFilm=(Film) titolo;
                stTitolo.setInt(7,(titoloFilm.getDurataMinuti()));
            }
            stTitolo.setInt(8,titolo.getAnnoPubblicazione());
            stTitolo.executeUpdate();

            if(titolo.getGeneriPresenti()!=null){
                for(Genere generi:titolo.getGeneriPresenti()){
                    stGeneri.setInt(1,titolo.getIdTitolo());
                    stGeneri.setInt(2, generi.getIdGenere());
                    stGeneri.addBatch();
                }
                stGeneri.executeBatch();
            }
            if(titolo.getPiattaforme()!=null){
                for(Piattaforma piattaforma:titolo.getPiattaforme()){
                    stPiattafome.setInt(1,titolo.getIdTitolo());
                    stPiattafome.setInt(2, piattaforma.getIdPiattaforma());
                    stPiattafome.addBatch();
                }
                stPiattafome.executeBatch();
            }
            con.commit();
            return true;
        } catch (Exception e){
            con.rollback();
            e.printStackTrace();
            System.out.println("Errore inserimento titolo nel DB");
            return false;

        }finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public boolean rimuoviTitolo(Titolo titolo) {
        String query="DELETE FROM titolo WHERE id_titolo=?";
        try(PreparedStatement st=con.prepareStatement(query)){
            st.setInt(1,titolo.getIdTitolo());
            int riga=st.executeUpdate();
            if (riga>0){
                System.out.println("Titolo rimosso dal DB");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Errore rimuovi titolo DB");
        return false;
    }

    /**
     * Funzione per cercare, all'interno del DB interno, uno o più titoli per nome.
     *
     * @param nomeTitolo
     * @return La funzione ritorna una lista di titoli, se non è presente alcun titolo con il nome indicato restituisce null.
     */
    @Override
    public List<Titolo> restituisciTitoloPerNome(String nomeTitolo) {
        List<Titolo> titoli = new ArrayList<>();
        List<Integer> genereAggiunti = new ArrayList<>();
        List<Integer> piattaformeAggiunte = new ArrayList<>();
        Map<Integer,Titolo> titoliMap=new HashMap<>();
        String query="SELECT * FROM titolo t LEFT JOIN trasmessosu tr on t.id_titolo=tr.id_titolo LEFT JOIN appartiene a on a.id_titolo=t.id_titolo" +
                " LEFT JOIN genere g on g.id_genere=a.id_genere LEFT JOIN piattaforma p on p.id_piattaforma=tr.id_piattaforma  WHERE nome_titolo=? ORDER BY t.id_titolo";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,nomeTitolo.toLowerCase());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                int idTitolo=rs.getInt("id_titolo");
                Titolo titolo=titoliMap.get(idTitolo);
                if(titolo==null){
                    if(rs.getString("tipologia").equals("SerieTv")){
                        titolo=new SerieTV(rs.getInt("id_titolo"),rs.getString("nome_titolo"),
                                rs.getString("trama"), rs.getString("immagine"),
                                rs.getDouble("voto_medio"),rs.getInt("anno_pubblicazione"));
                    }
                    else {
                        titolo=new Film(rs.getInt("id_titolo"),rs.getString("nome_titolo"),
                                rs.getString("trama"), rs.getString("immagine"),
                                rs.getDouble("voto_medio"),rs.getInt("durata_minuti"),
                                rs.getInt("anno_pubblicazione"));
                    }
                    genereAggiunti.clear();
                    piattaformeAggiunte.clear();
                    titoliMap.put(idTitolo,titolo);
                }
                int idGenere=rs.getInt("id_genere");
                boolean genereNull=rs.wasNull();
                String nomeGenere=rs.getString("nome_genere");
                if(!genereNull && !genereAggiunti.contains(idGenere)){
                    Genere genere=new Genere(nomeGenere,idGenere);
                    titolo.aggiungiGenere(genere);
                    genereAggiunti.add(idGenere);
                }
                int idPiattaforma=rs.getInt("id_piattaforma");
                boolean piattaformaNull=rs.wasNull();
                String nomePiattaforma=rs.getString("nome_piattaforma");
                if(!piattaformaNull && !piattaformeAggiunte.contains(idPiattaforma)){
                    Piattaforma piattaforma = new Piattaforma(nomePiattaforma,idPiattaforma);
                    titolo.aggiungiPiattaforme(piattaforma);
                    piattaformeAggiunte.add(idPiattaforma);
                }
            }
            titoli.addAll(titoliMap.values());
            System.out.println("Titoli trovati " + nomeTitolo);
            return titoli;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("nessun titolo trovato "+nomeTitolo);
        return null;
    }

    @Override
    public List<Genere> restituisciGeneriTitolo(Integer idTitolo) {
        List<Genere> generi = new ArrayList<>();
        String query="SELECT * FROM appartiene a JOIN genere g on a.id_genere=g.id_genere WHERE id_titolo=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idTitolo);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Genere genere=new Genere(rs.getString("nome_genere"),rs.getInt("id_genere"));
                generi.add(genere);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return generi;
    }
}
