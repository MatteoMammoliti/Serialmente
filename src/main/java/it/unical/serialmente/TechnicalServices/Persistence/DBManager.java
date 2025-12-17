package it.unical.serialmente.TechnicalServices.Persistence;

import it.unical.serialmente.TechnicalServices.Persistence.dao.*;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

    private static DBManager instance;
    private Connection con;

    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    static {
        try {
            String envUrl = System.getenv("SERIALMENTE_DB_URL");
            String envUser = System.getenv("SERIALMENTE_DB_USERNAME");
            String envPass = System.getenv("SERIALMENTE_DB_PASSWORD");

            boolean envValid =
                    envUrl != null && !envUrl.isBlank() &&
                            envUser != null && !envUser.isBlank() &&
                            envPass != null && !envPass.isBlank();

            if (envValid) {
                DB_URL = envUrl;
                DB_USER = envUser;
                DB_PASSWORD = envPass;

            } else {
                try (InputStream input = DBManager.class.getResourceAsStream("/config.properties")) {

                    if (input == null) {
                        throw new RuntimeException("File config.properties non trovato nel JAR");
                    }

                    Properties prop = new Properties();
                    prop.load(input);

                    DB_URL = prop.getProperty("db.url");
                    DB_USER = prop.getProperty("db.username");
                    DB_PASSWORD = prop.getProperty("db.password");

                    if (DB_URL == null || DB_USER == null || DB_PASSWORD == null ||
                            DB_URL.isBlank() || DB_USER.isBlank() || DB_PASSWORD.isBlank()) {

                        throw new RuntimeException("Credenziali DB mancanti in config.properties");
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore caricamento configurazione DB", e);
        }
    }

    private DBManager() {}

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore connessione DB", e);
        }
        return con;
    }

    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                throw new RuntimeException("Errore chiusura connessione DB", e);
            }
        }
    }

    public UtenteDAO getUtenteDAO() { return new UtenteDAOPostgres(getConnection()); }
    public CredenzialiUtenteDAO getCredenzialiUtenteDAO() { return new CredenzialiUtenteDAOPostgres(getConnection()); }
    public TitoloDAO getTitoloDAO() { return new TitoloDAOPostgres(getConnection()); }
}