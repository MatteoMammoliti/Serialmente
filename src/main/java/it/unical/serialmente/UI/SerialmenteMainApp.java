package it.unical.serialmente.UI;

import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.CredenzialiUtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.SelezioneTitoloDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.TitoloDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.UtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.model.*;
import it.unical.serialmente.TechnicalServices.Persistence.service.UtenteService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SerialmenteMainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        DBManager db=DBManager.getInstance();
        SelezioneTitoloDAO selezione=db.SelezioneTitoloDAO();
        Utente u=new Utente();
        u.setIdUtente(3);
        SessioneCorrente.setUtenteCorrente(u);

        HashSet<Genere> generi=new HashSet();
        generi=selezione.restituisciGeneriVisionati();
        for(Genere g : generi){
            System.out.println(g.getNomeGenere());
        }





        
    }
}