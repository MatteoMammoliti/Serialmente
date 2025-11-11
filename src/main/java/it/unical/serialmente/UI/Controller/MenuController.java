package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.UI.Model.ModelContainerMenuPagine;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

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
                ModelContainerMenuPagine.getInstance().getViewFactory().getFinestraAttuale().set("Film")
        );

        this.serieTV.setOnAction(_ ->
                ModelContainerMenuPagine.getInstance().getViewFactory().getFinestraAttuale().set("SerieTV")
        );

        this.watchlist.setOnAction(_ ->
                ModelContainerMenuPagine.getInstance().getViewFactory().getFinestraAttuale().set("Watchlist")
        );

        this.areaUtente.setOnAction(_ ->
                ModelContainerMenuPagine.getInstance().getViewFactory().getFinestraAttuale().set("ProfiloUtente")
        );

        this.logout.setOnAction(_ ->
                ModelContainerMenuPagine.getInstance().getViewFactory().getFinestraAttuale().set("Logout")
        );
    }
}