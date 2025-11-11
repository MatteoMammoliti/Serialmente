package it.unical.serialmente.UI;

import it.unical.serialmente.UI.Model.ModelContainerMenuPagine;
import it.unical.serialmente.UI.View.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class SerialmenteMainApp extends Application {
    @Override
    public void start(Stage stage) {
        ViewFactory viewFactory = ModelContainerMenuPagine.getInstance().getViewFactory();
        viewFactory.mostraFinestraLogin();
    }
}