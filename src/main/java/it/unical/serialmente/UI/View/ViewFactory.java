package it.unical.serialmente.UI.View;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewFactory {

    private final StringProperty finestraAttuale;
    private BorderPane homePage;
    private BorderPane Watchlist;
    private BorderPane paginaRicerca;
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
        FXMLLoader finestraHome = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/homePage.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(finestraHome.load());
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.nuovoAlert(
                    "Errore!",
                    Alert.AlertType.ERROR,
                    "Errore durante l'apertura della pagina",
                    "Qualcosa è andato storto durante l'apertura della Home"
            );
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Serialmente - HomePage");
        stage.show();
    }

    public BorderPane getHomePageSenzaMenu() {return null;}

    public BorderPane getWatchlist() { return null; }

    public BorderPane getPaginaRicerca() { return null; }

    public BorderPane getPaginaProfiloUtente() { return null; }

    /**
     * Funzione per chiudere la pagina che si sta lasciando durante una transizione
     * @param stage
     */
    public void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}