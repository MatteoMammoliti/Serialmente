package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.UI.Model.ModelView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @FXML private BorderPane contenitoreView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Ascolta i cambi di finestra
        ModelView.getInstance().getViewFactory().getFinestraAttuale().addListener((observable, oldValue, newValue) -> {

            Node nuovaView = switch (newValue) {
                case "Film" -> ModelView.getInstance().getViewFactory().getPaginaFilm();
                case "SerieTV" -> ModelView.getInstance().getViewFactory().getPaginaSerieTV();
                case "Watchlist" -> ModelView.getInstance().getViewFactory().getWatchlist();
                case "ProfiloUtente" -> ModelView.getInstance().getViewFactory().getPaginaProfiloUtente();
                case "Logout" -> {
                    SessioneCorrente.resetSessioneCorrente();
                    yield null;
                }
                default -> null;
            };

            if (nuovaView != null) {
                contenitoreView.setCenter(nuovaView);
            }
        });
    }
}