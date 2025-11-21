package it.unical.serialmente;

import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Utility.ThreadPool;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.View.ViewFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class SerialmenteMainApp extends Application {
    @Override
    public void start(Stage stage) {
        ViewFactory viewFactory = ModelContainerView.getInstance().getViewFactory();
        viewFactory.mostraFinestraLogin();
    }

    @Override
    public void stop() {
        ThreadPool.shutdown();
        DBManager.getInstance().closeConnection();
        Platform.exit();
        System.exit(0);
    }
}