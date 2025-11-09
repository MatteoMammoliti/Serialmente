package it.unical.serialmente.UI;

import it.unical.serialmente.UI.Model.ModelView;
import it.unical.serialmente.UI.View.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SerialmenteMainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        ViewFactory viewFactory = ModelView.getInstance().getViewFactory();
        viewFactory.mostraFinestraRegistrazione();
    }
}