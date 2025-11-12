package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.UI.Model.ModelContainerMenuPagine;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class ContainerMenuPagineController implements Initializable {

    @FXML private BorderPane contenitoreView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ModelContainerMenuPagine.getInstance().setMenuPagineController(this);
        ModelContainerMenuPagine.getInstance().getViewFactory().getFinestraAttuale().addListener((observable, oldValue, newValue) -> {

            Node nuovaView = switch (newValue) {
                case "Film" -> ModelContainerMenuPagine.getInstance().getViewFactory().getPaginaFilm();
                case "SerieTV" -> ModelContainerMenuPagine.getInstance().getViewFactory().getPaginaSerieTV();
                case "Watchlist" -> ModelContainerMenuPagine.getInstance().getViewFactory().getWatchlist();
                case "ProfiloUtente" -> ModelContainerMenuPagine.getInstance().getViewFactory().getPaginaProfiloUtente();
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