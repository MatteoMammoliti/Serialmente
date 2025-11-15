package it.unical.serialmente.TechnicalServices.Persistence.dao.postgres;

import it.unical.serialmente.TechnicalServices.Persistence.dao.ProgressoSerieDAO;
import it.unical.serialmente.Domain.model.*;

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


    @Override
    public void cambiaEpisodioCorrente(Integer idUtente, Integer idSerieTV, String descrizioneEpisodio, Integer durataEpisodio, Integer idEpisodioProssimo, Integer numeroProgressivoEpisodio) {
        String query = "UPDATE progressoserie SET id_episodio=?,descrizione_episodio=?,durata_minuti_episodio=?, numero_progressivo_episodio = ?  WHERE id_serie=? AND id_utente=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idEpisodioProssimo);
            st.setString(2, descrizioneEpisodio);
            st.setInt(3, durataEpisodio);
            st.setInt(4, numeroProgressivoEpisodio);
            st.setInt(5, idSerieTV);
            st.setInt(6, idUtente);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiaStagioneCorrente(Integer idUtente, Integer idSerieTV, Integer annoPubblicazione, Integer idStagioneProssima, Integer numeroProgressivoStagione) {
        String query = "UPDATE progressoserie SET id_stagione=?,anno_pubblicazione_stagione=?, numero_progressivo_stagione = ?, numero_progressivo_episodio = ? WHERE id_serie=? AND id_utente=?";
        try (PreparedStatement rs = conn.prepareStatement(query)) {
            rs.setInt(1, idStagioneProssima);
            rs.setInt(2, annoPubblicazione);
            rs.setInt(5, idSerieTV);
            rs.setInt(6, idUtente);
            rs.setInt(3, numeroProgressivoStagione);
            rs.setInt(4, 1);
            rs.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean creaIstanzaProgressoSerie(Integer idUtente, Integer idTitolo, Integer idEpisodio, Integer idStagione, Integer annoPubblicazioneStagione, String descrizioneEpisodio, Integer durataMinuti, Integer numeroProgressivoEpisodio, Integer numeroProgressivoStagione) {
        String query = "INSERT INTO progressoserie(id_utente, id_serie, id_stagione, id_episodio, anno_pubblicazione_stagione, descrizione_episodio, durata_minuti_episodio, numero_progressivo_stagione, numero_progressivo_episodio) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idTitolo);
            st.setInt(3, idStagione);
            st.setInt(4, idEpisodio);
            st.setInt(5, annoPubblicazioneStagione);
            st.setString(6, descrizioneEpisodio);
            st.setInt(7, durataMinuti);
            st.setInt(8, numeroProgressivoStagione);
            st.setInt(9, numeroProgressivoEpisodio);
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer getIdEpisodioCorrente(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT id_episodio,descrizione_episodio,durata_minuti_episodio FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getInt("id_episodio");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ContenitoreDatiProgressoSerie getDatiCorrenti(Integer idUtente, Titolo titolo) {
        String query = "SELECT id_stagione, id_episodio, numero_progressivo_stagione FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, titolo.getIdTitolo());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new ContenitoreDatiProgressoSerie(rs.getInt("id_stagione"), rs.getInt("id_episodio"), rs.getInt("numero_progressivo_stagione"));
            }
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
            if (rs.next()) {
                return rs.getInt("id_stagione");
            }
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
            if (rs.next()) {
                return rs.getInt("numero_progressivo_stagione");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getNomeEpisodio(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT nome_episodio FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                return rs.getString("nome_episodio");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Integer> getStagioniInCorso(Integer idUtente) {
        List<Integer> idSerie = new ArrayList<>();
        String query = "SELECT id_serie FROM progressoserie WHERE id_utente=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                idSerie.add(rs.getInt("id_serie"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idSerie;
    }

    @Override
    public boolean controlloSerieTvInCorso(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT 1 FROM  progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public Integer getNumeroProgressivoEpisodio(Integer idUtente, Integer idSerieTV) {
        String query = "SELECT numero_progressivo_episodio FROM progressoserie WHERE id_utente=? AND id_serie=?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setInt(1, idUtente);
            st.setInt(2, idSerieTV);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getInt("numero_progressivo_episodio");
            }
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
            if (i > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}
