package it.unical.serialmente.TechnicalServices.Persistence;

import it.unical.serialmente.TechnicalServices.Persistence.dao.*;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.*;

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
    public CredenzialiUtenteDAO getCredenzialiUtenteDAO(){
        return new CredenzialiUtenteDAOPostgres(getConnection());
    }
    public TitoloDAO getTitoloDAO(){
        return new TitoloDAOPostgres(getConnection());
    }
    public SelezioneTitoloDAO SelezioneTitoloDAO(){
        return new SelezioneTitoloDAOPostgres(getConnection());
    }
    public ProgressoSerieDAO getProgressoSerieDAO(){
        return new ProgressoSerieDAOPostgres(getConnection());
    }
}
