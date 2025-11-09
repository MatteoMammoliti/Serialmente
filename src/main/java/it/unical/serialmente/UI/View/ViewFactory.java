package it.unical.serialmente.UI.View;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    private final StringProperty finestraAttuale;
    private BorderPane homePage;
    private BorderPane watchlist;
    private BorderPane paginaProfiloUtente;

    public ViewFactory() {
        this.finestraAttuale = new SimpleStringProperty("");
    }

    /**
     * Funzione per ottenere l'attuale pagina in cui si trova l'utente
     * @return StringProperty finestraAttuale
     */
    public StringProperty getFinestraAttuale() {
        return finestraAttuale;
    }

    public void mostraFinestraRegistrazione() {

        FXMLLoader finestraRegistrazione = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/registrazione.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(finestraRegistrazione.load());
        } catch (Exception e) {
            AlertHelper.nuovoAlert(
                    "Errore!",
                    Alert.AlertType.ERROR,
                    "Errore durante l'apertura della pagina",
                    "Qualcosa è andato storto durante l'apertura della pagina di registrazione"
            );
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Serialmente - Registrazione");
        stage.setResizable(false);
        stage.show();
    }

    public void mostraFinestraLogin() {

        FXMLLoader finestraLogin = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/login.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(finestraLogin.load());
        } catch (Exception e) {
            AlertHelper.nuovoAlert(
                    "Errore!",
                    Alert.AlertType.ERROR,
                    "Errore durante l'apertura della pagina",
                    "Qualcosa è andato storto durante l'apertura della pagina di login"
            );
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Serialmente - Login");
        stage.setResizable(false);
        stage.show();
    }

    public void mostraHomePageConMenu(){

        FXMLLoader homePage = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/HomePage.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(homePage.load());
        } catch (Exception e) {
            AlertHelper.nuovoAlert(
                    "Errore!",
                    Alert.AlertType.ERROR,
                    "Errore durante l'apertura della pagina",
                    "Qualcosa è andato storto durante l'apertura della HomePage"
            );
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Serialmente - HomePage");
        stage.setResizable(false);
        stage.show();
    }

    public BorderPane getHomePageSenzaMenu() {

        if (homePage == null) {
            try {
                homePage = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/HomePage.fxml")).load();
            } catch (IOException e) {
                AlertHelper.nuovoAlert(
                        "Errore!",
                        Alert.AlertType.ERROR,
                        "Errore durante l'apertura della pagina",
                        "Qualcosa è andato storto durante l'apertura della HomePage"
                );
            }
        }
        else if(homePage.getParent() != null){
            ((javafx.scene.layout.Pane) homePage.getParent()).getChildren().remove(homePage);
        }
        return homePage;
    }

    public BorderPane getWatchlist() {

        if (watchlist == null) {
            try {
                watchlist = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Watchlist.fxml")).load();
            } catch (IOException e) {
                AlertHelper.nuovoAlert(
                        "Errore!",
                        Alert.AlertType.ERROR,
                        "Errore durante l'apertura della pagina",
                        "Qualcosa è andato storto durante l'apertura della Watchlist"
                );
            }
        }
        else if(watchlist.getParent() != null){
            ((javafx.scene.layout.Pane) homePage.getParent()).getChildren().remove(watchlist);
        }
        return watchlist;
    }


    public BorderPane getPaginaProfiloUtente() {

        if (paginaProfiloUtente == null) {
            try {
                watchlist = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/profiloUtente.fxml")).load();
            } catch (IOException e) {
                AlertHelper.nuovoAlert(
                        "Errore!",
                        Alert.AlertType.ERROR,
                        "Errore durante l'apertura della pagina",
                        "Qualcosa è andato storto durante l'apertura della pagina di profilo utente"
                );
            }
        }
        else if(paginaProfiloUtente.getParent() != null){
            ((javafx.scene.layout.Pane) paginaProfiloUtente.getParent()).getChildren().remove(paginaProfiloUtente);
        }
        return paginaProfiloUtente;
    }

    /**
     * Funzione per chiudere la pagina che si sta lasciando durante una transizione
     * @param stage
     */
    public void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }

    public void invalidateHomePage() { this.homePage = null; }
    public void invalidateWatchlist() { this.watchlist = null; }
    public void invalidatePaginaProfiloUtente() {  this.paginaProfiloUtente = null; }
}