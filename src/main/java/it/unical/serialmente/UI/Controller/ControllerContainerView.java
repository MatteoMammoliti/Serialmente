package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerContainerView implements Initializable {

    @FXML private BorderPane contenitoreView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ModelContainerView.getInstance().setMenuPagineController(this);
        ModelContainerView.getInstance().getViewFactory().getFinestraAttuale().addListener((observable, oldValue, newValue) -> {

            Node nuovaView = switch (newValue) {
                case "Film" -> ModelContainerView.getInstance().getViewFactory().getPaginaFilm();
                case "SerieTV" -> ModelContainerView.getInstance().getViewFactory().getPaginaSerieTV();
                case "Watchlist" -> ModelContainerView.getInstance().getViewFactory().getWatchlist();
                case "ProfiloUtente" -> ModelContainerView.getInstance().getViewFactory().getPaginaProfiloUtente();
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

    public BorderPane getContenitoreView() {
        return contenitoreView;
    }
}