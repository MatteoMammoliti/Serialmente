package it.unical.serialmente.UI;

import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.CredenzialiUtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.UtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.model.CredenzialiUtente;
import it.unical.serialmente.TechnicalServices.Persistence.model.Utente;
import it.unical.serialmente.TechnicalServices.Persistence.service.UtenteService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class SerialmenteMainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DBManager db=DBManager.getInstance();
        UtenteDAO utenteDao=db.getUtenteDAO();
        CredenzialiUtenteDAO credenzialiDAO=db.credenzialiUtenteDAO();
        UtenteService utenteService=new UtenteService(utenteDao,credenzialiDAO);

        Utente utente=utenteService.loginUtente("1@1","1234");
        CredenzialiUtente credenziali=utente.getCredenzialiUtente();
        Integer prova= credenzialiDAO.cercaIdUtente(credenziali.getEmail());
        System.out.println(prova);

        
    }
}