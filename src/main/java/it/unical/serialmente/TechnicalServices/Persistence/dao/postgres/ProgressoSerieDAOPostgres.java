package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.ProgressoSerieDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.SelezioneTitoloDAO;
import javafx.util.Pair;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgressoSerieDAOPostgres implements ProgressoSerieDAO {
    Connection conn;

    public ProgressoSerieDAOPostgres(Connection conn) {
        this.conn = conn;
    }

    private final SelezioneTitoloDAO selezioneDao =  new SelezioneTitoloDAOPostgres(
            DBManager.getInstance().getConnection()
    );

    @Override
    public void avanzaEpisodio(Integer idUtente, Integer idSerieTV, Integer durataEpisodio, Integer idEpisodioProssimo) {
        String query = "UPDATE progressoserie SET id_episodio=?,durata_minuti_episodio=?, numero_progressivo_episodio = numero_progressivo_episodio + 1, minuti_visti = minuti_visti + durata_minuti_episodio, numero_episodi_visti = numero_episodi_visti + 1 WHERE id_serie=? AND id_utente=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idEpisodioProssimo);
            st.setInt(2, durataEpisodio);
            st.setInt(3, idSerieTV);
            st.setInt(4, idUtente);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * la funzione ha il compito di avanzare all'episodio successivo di un serie Tv non appena quest'ultimo viene visualizzato.
     * Nel caso in cui l'episodio sia l'ultimo della serie, il titolo in questione viene eliminato da "progresso serie" e viene aggiunto all'interno della lista
     * "Visionati" dell'utente con annessi minuti visti e episodi visti complessivi dell'intera serie.
     * @param idUtente Id dell'utente interessato.
     * @param idSerieTV Id della SerieTv
     * @param durataEpisodio Durata in minuti dell'episodio appena visionato.
     * @param idEpisodioProssimo Id dell'episodio successivo a quello appena visionato.
     * @param idProssimaStagione Id della stagione successiva, qualora ve ne sia una, sarà 0 altrimenti.
     * @throws SQLException
     */
    @Override
    public void avanzaEpisodioEstagione(Integer idUtente, Integer idSerieTV, Integer durataEpisodio, Integer idEpisodioProssimo, Integer idProssimaStagione) throws SQLException {

        int minuti_visti = 0;
        int numeroEpisodiVisti = 0;

        String query = """
        UPDATE progressoserie
        SET\s
            id_episodio = ?,
            durata_minuti_episodio = ?,         \s
            numero_progressivo_episodio = 1,
            minuti_visti = minuti_visti + ?,    \s
            numero_episodi_visti = numero_episodi_visti + 1,
            id_stagione = ?,
            numero_progressivo_stagione = numero_progressivo_stagione + 1
        WHERE id_serie = ? AND id_utente = ?
        RETURNING minuti_visti, numero_episodi_visti
       \s""";

        DBManager.getInstance().getConnection().setAutoCommit(false);

        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idEpisodioProssimo);
            st.setInt(2, durataEpisodio);
            st.setInt(3, durataEpisodio);
            st.setInt(4, idProssimaStagione);
            st.setInt(5, idSerieTV);
            st.setInt(6, idUtente);

            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) {
                    DBManager.getInstance().getConnection().rollback();
                    return;
                }
                minuti_visti = rs.getInt("minuti_visti");
                numeroEpisodiVisti = rs.getInt("numero_episodi_visti");
            }

            if (idProssimaStagione == 0) {
                boolean successo = eliminaSerieDaProgressioSerie(idUtente, idSerieTV);
                if (!successo) {
                    DBManager.getInstance().getConnection().rollback();
                    return;
                }

                successo = selezioneDao.aggiungiTitoloInLista(
                        idUtente,
                        idSerieTV,
                        "Visionati",
                        minuti_visti,
                        numeroEpisodiVisti
                );
                if (!successo) {
                    DBManager.getInstance().getConnection().rollback();
                    return;
                }
            }

            DBManager.getInstance().getConnection().commit();

        } catch (Exception e) {
            DBManager.getInstance().getConnection().rollback();
            e.printStackTrace();
        } finally {
            DBManager.getInstance().getConnection().setAutoCommit(true);
        }
    }

    @Override
    public boolean creaIstanzaProgressoSerie(Integer idUtente, Integer idTitolo, Integer idEpisodio, Integer idStagione, Integer durataMinuti, Integer numeroProgressivoEpisodio, Integer numeroProgressivoStagione) {
        String query = "INSERT INTO progressoserie(id_utente, id_serie, id_stagione, id_episodio, durata_minuti_episodio, numero_progressivo_stagione, numero_progressivo_episodio) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idTitolo);
            st.setInt(3, idStagione);
            st.setInt(4, idEpisodio);
            st.setInt(5, durataMinuti);
            st.setInt(6, numeroProgressivoStagione);
            st.setInt(7, numeroProgressivoEpisodio);
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer getIdEpisodioCorrente(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT id_episodio,durata_minuti_episodio FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);

            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt("id_episodio");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer getIdStagioneCorrente(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT id_stagione FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);
            ResultSet rs = st.executeQuery();

            if (rs.next()) return rs.getInt("id_stagione");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer getNumeroProgressivoStagione(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT numero_progressivo_stagione FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);
            ResultSet rs = st.executeQuery();

            if (rs.next()) return rs.getInt("numero_progressivo_stagione");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean controlloSerieTvInCorso(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT 1 FROM  progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);
            ResultSet rs = st.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getNumeroProgressivoEpisodio(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT numero_progressivo_episodio FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);

            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt("numero_progressivo_episodio");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean eliminaSerieDaProgressioSerie(Integer idUtente, Integer idSerieTV) {
        String query = "DELETE FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);

            int i = st.executeUpdate();
            return i > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Integer> getIdSerieTvInVisione(Integer idUtente) {
        List<Integer> p = new ArrayList<>();
        String query = "SELECT id_serie FROM progressoserie WHERE id_utente = ?";

        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                p.add(rs.getInt("id_serie"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public Pair<Integer, Integer> getStatisticheSerieInVisione(Integer idUtente, Integer idSerie) {
        String query = "SELECT minuti_visti, numero_episodi_visti FROM progressoserie WHERE id_utente = ?  AND id_serie = ?";

        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerie);
            ResultSet rs = st.executeQuery();

            if(rs.next()) return new Pair<>(rs.getInt("minuti_visti"), rs.getInt("numero_episodi_visti"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}