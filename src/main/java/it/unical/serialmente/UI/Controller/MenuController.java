package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.UI.Model.ModelView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private Button homePage;
    @FXML private Button watchlist;
    @FXML private Button areaUtente;
    @FXML private Button logout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aggiungiListenersPulsanti();
    }

    private void aggiungiListenersPulsanti() {
        this.homePage.setOnAction(_ ->
                ModelView.getInstance().getViewFactory().getFinestraAttuale().set("HomePage")
        );

        this.watchlist.setOnAction(_ ->
                ModelView.getInstance().getViewFactory().getFinestraAttuale().set("Watchlist")
        );

        this.areaUtente.setOnAction(_ ->
                ModelView.getInstance().getViewFactory().getFinestraAttuale().set("ProfiloUtente")
        );

        this.logout.setOnAction(_ ->
                ModelView.getInstance().getViewFactory().getFinestraAttuale().set("Logout")
        );
    }
}