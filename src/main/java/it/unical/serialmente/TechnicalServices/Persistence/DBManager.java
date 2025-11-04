package it.unical.serialmente.TechnicalServices.Persistence;

import it.unical.serialmente.TechnicalServices.Persistence.dao.CredenzialiUtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.SelezioneTitoloDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.TitoloDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.UtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.CredenzialiUtenteDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.SelezioneTitoloDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.TitoloDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.UtenteDAOPostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static DBManager instance;
    Connection con;

    private DBManager() {}

    public static  DBManager getInstance(){
        if (instance==null){
            instance=new DBManager();
        }
        return instance;
    }

    public Connection getConnection(){
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection("jdbc:postgresql://ep-long-darkness-ag5midlz-pooler.c-2.eu-central-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require", "neondb_owner", "npg_xaru1sRD4oke");
            }
        }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return con;
    }

    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public UtenteDAO getUtenteDAO(){
        return new UtenteDAOPostgres(getConnection());
    }
    public CredenzialiUtenteDAO credenzialiUtenteDAO(){
        return new CredenzialiUtenteDAOPostgres(getConnection());
    }
    public TitoloDAO getTitoloDAO(){
        return new TitoloDAOPostgres(getConnection());
    }
    public SelezioneTitoloDAO SelezioneTitoloDAO(){
        return new SelezioneTitoloDAOPostgres(getConnection());
    }
}
