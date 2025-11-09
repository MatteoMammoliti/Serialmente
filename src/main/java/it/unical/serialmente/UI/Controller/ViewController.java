package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.UI.Model.ModelView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @FXML private BorderPane contenitoreView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ModelView.getInstance().getViewFactory().getFinestraAttuale().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "HomePage" -> contenitoreView.setCenter(
                        ModelView.getInstance().getViewFactory().getHomePageSenzaMenu()
                );

                case "Logout" -> SessioneCorrente.resetSessioneCorrente();

                case "Watchlist" -> contenitoreView.setCenter(
                        ModelView.getInstance().getViewFactory().getWatchlist()
                );

                case "ProfiloUtente" ->  contenitoreView.setCenter(
                        ModelView.getInstance().getViewFactory().getPaginaProfiloUtente()
                );
            }
        });
    }
}