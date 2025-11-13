package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMenu implements Initializable {

    @FXML private Button film;
    @FXML private Button serieTV;
    @FXML private Button watchlist;
    @FXML private Button areaUtente;
    @FXML private Button logout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aggiungiListenersPulsanti();
    }

    private void aggiungiListenersPulsanti() {
        this.film.setOnAction(_ ->
                ModelContainerView.getInstance().getViewFactory().getFinestraAttuale().set("Film")
        );

        this.serieTV.setOnAction(_ ->
                ModelContainerView.getInstance().getViewFactory().getFinestraAttuale().set("SerieTV")
        );

        this.watchlist.setOnAction(_ ->
                ModelContainerView.getInstance().getViewFactory().getFinestraAttuale().set("Watchlist")
        );

        this.areaUtente.setOnAction(_ ->
                ModelContainerView.getInstance().getViewFactory().getFinestraAttuale().set("ProfiloUtente")
        );

        this.logout.setOnAction(_ -> {
            ModelContainerView.getInstance().getViewFactory().getFinestraAttuale().set("Logout");
            ModelContainerView.getInstance().getViewFactory().closeStage(
                    (Stage) this.areaUtente.getScene().getWindow()
            );
        });
    }
}