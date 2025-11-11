package it.unical.serialmente.UI;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Utente;
import it.unical.serialmente.UI.Model.ModelView;
import it.unical.serialmente.UI.View.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class SerialmenteMainApp extends Application {
    @Override
    public void start(Stage stage) {
        ViewFactory viewFactory = ModelView.getInstance().getViewFactory();
        Utente u = new Utente();
        u.setIdUtente(1);
        SessioneCorrente.setUtenteCorrente(u);
        viewFactory.mostraPaginaFilmConMenu();
    }
}