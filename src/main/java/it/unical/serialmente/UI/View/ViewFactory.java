package it.unical.serialmente.UI.View;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.UI.Controller.PagineNavigazione.ControllerSezioneUtente;
import it.unical.serialmente.UI.Controller.PagineNavigazione.ControllerWatchlist;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class ViewFactory {

    private final StringProperty finestraAttuale;
    private BorderPane paginaFilm;
    private BorderPane paginaSerieTV;
    private BorderPane watchlist;
    private BorderPane paginaProfiloUtente;
    private GrigliaTitoli grigliaTitoli;
    private ControllerWatchlist controllerWatchlist;
    private ControllerSezioneUtente controllerSezioneUtente;

    private final Deque<Parent> navigazione = new ArrayDeque<>();


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

    public void setPaginaPrecedente(Parent paginaPrecedente, ControllerWatchlist controller) {
        navigazione.push(paginaPrecedente);

        if(controller != null && this.controllerSezioneUtente != null) {
            this.controllerWatchlist = controller;
        }
    }

    public void tornaAllaPaginaPrecedente() {
        if (navigazione.isEmpty()) {
            return;
        }

        Parent paginaPrecedente = navigazione.pop();

        Stage stageCorrente = (Stage) Stage.getWindows()
                .stream()
                .filter(Window::isShowing)
                .findFirst()
                .orElse(null);

        if (stageCorrente != null) {
            stageCorrente.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unical/serialmente/UI/Images/DecorazioniApp/favicon.png"))));
            stageCorrente.getScene().setRoot(paginaPrecedente);

            if(this.controllerWatchlist != null) {
                controllerWatchlist.refresh();
            }
        }
    }

    public void mostraFinestraRegistrazione() {

        FXMLLoader finestraRegistrazione = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/Autenticazione/registrazione.fxml"));
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
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unical/serialmente/UI/Images/DecorazioniApp/favicon.png"))));
        stage.setResizable(false);
        stage.show();
    }

    public void mostraPaginaRecuperaPassword(){
        FXMLLoader finestraRecupero = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/Autenticazione/RecuperoPassword/cambioPassword.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(finestraRecupero.load());
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.nuovoAlert(
                    "Errore!",
                    Alert.AlertType.ERROR,
                    "Errore durante l'apertura della pagina",
                    "Qualcosa è andato storto durante l'apertura della pagina di recupero Password"
            );
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Serialmente - Recupero password");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unical/serialmente/UI/Images/DecorazioniApp/favicon.png"))));
        stage.setResizable(false);
        stage.show();
    }

    public void mostraFinestraLogin() {

        FXMLLoader finestraLogin = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/Autenticazione/login.fxml"));
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
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unical/serialmente/UI/Images/DecorazioniApp/favicon.png"))));
        stage.setResizable(false);
        stage.show();
    }

    public void mostraPaginaPreferenze(Stage stage) {
        FXMLLoader finestraPreferenze = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/Autenticazione/sceltaPreferenze.fxml"));
        Parent root = null;

        try {
            root = finestraPreferenze.load();
        } catch (Exception e) {
            AlertHelper.nuovoAlert(
                    "Errore!",
                    Alert.AlertType.ERROR,
                    "Errore durante l'apertura della pagina",
                    "Qualcosa è andato storto durante l'apertura della pagina di scelta delle preferenze"
            );
        }

        stage.getScene().setRoot(root);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unical/serialmente/UI/Images/DecorazioniApp/favicon.png"))));
    }

    public void mostraPaginaFilmConMenu(){

        FXMLLoader homePage = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/ContainerView.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(homePage.load());
            scene.getStylesheets().add(
                    getClass().getResource("/it/unical/serialmente/UI/CSS/pagineFilm.css").toExternalForm()
            );
        } catch (Exception e) {
            AlertHelper.nuovoAlert(
                    "Errore!",
                    Alert.AlertType.ERROR,
                    "Errore durante l'apertura della pagina",
                    "Qualcosa è andato storto durante l'apertura della HomePage Film"
            );
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Serialmente - HomePage");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unical/serialmente/UI/Images/DecorazioniApp/favicon.png"))));

        stage.setWidth(1280);
        stage.setHeight(720);
        stage.centerOnScreen();

        stage.show();
    }

    public BorderPane getPaginaFilm() {

        if (paginaFilm == null) {
            try {
                paginaFilm = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/PagineNavigazione/paginaFilm.fxml")).load();
            } catch (IOException e) {
                AlertHelper.nuovoAlert(
                        "Errore!",
                        Alert.AlertType.ERROR,
                        "Errore durante l'apertura della pagina",
                        "Qualcosa è andato storto durante l'apertura della HomePage Film"
                );
            }
        }
        else if(paginaFilm.getParent() != null){
            ((javafx.scene.layout.Pane) paginaFilm.getParent()).getChildren().remove(paginaFilm);
        }
        this.invalidateGrigliaTitoli();
        return paginaFilm;
    }

    public BorderPane getPaginaSerieTV() {

        if (paginaSerieTV == null) {
            try {
                paginaSerieTV = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/PagineNavigazione/paginaSerieTV.fxml")).load();
            } catch (IOException e) {
                AlertHelper.nuovoAlert(
                        "Errore!",
                        Alert.AlertType.ERROR,
                        "Errore durante l'apertura della pagina",
                        "Qualcosa è andato storto durante l'apertura della HomePage Serie TV"
                );
            }
        }
        else if(paginaSerieTV.getParent() != null){
            ((javafx.scene.layout.Pane) paginaSerieTV.getParent()).getChildren().remove(paginaSerieTV);
        }
        this.invalidateGrigliaTitoli();
        return paginaSerieTV;
    }


    public BorderPane getWatchlist() {

        if (watchlist == null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/it/unical/serialmente/UI/Fxml/PagineNavigazione/Watchlist.fxml")
                );
                watchlist = loader.load();

                controllerWatchlist = loader.getController();


            } catch (IOException e) {
                e.printStackTrace();
                AlertHelper.nuovoAlert(
                        "Errore!",
                        Alert.AlertType.ERROR,
                        "Errore durante l'apertura della pagina",
                        "Qualcosa è andato storto durante l'apertura della Watchlist"
                );
            }
        }
        else if(watchlist.getParent() != null){
            ((javafx.scene.layout.Pane) watchlist.getParent()).getChildren().remove(watchlist);
        }
        if(controllerWatchlist != null){
            controllerWatchlist.refresh();
        }
        this.invalidateGrigliaTitoli();
        return watchlist;
    }


    public BorderPane getPaginaProfiloUtente() throws Exception {

        if (paginaProfiloUtente == null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/it/unical/serialmente/UI/Fxml/PagineNavigazione/profiloUtente.fxml")
                );
                paginaProfiloUtente = loader.load();

                controllerSezioneUtente = loader.getController();

            } catch (IOException e) {
                AlertHelper.nuovoAlert(
                        "Errore!",
                        Alert.AlertType.ERROR,
                        "Errore durante l'apertura della pagina",
                        "Qualcosa è andato storto durante l'apertura della pagina di profilo utente"
                );
                e.printStackTrace();
            }
        }
        else if(paginaProfiloUtente.getParent() != null){
            ((javafx.scene.layout.Pane) paginaProfiloUtente.getParent()).getChildren().remove(paginaProfiloUtente);
        }
        if(controllerSezioneUtente != null){
            controllerSezioneUtente.refresh();
        }
        this.invalidateGrigliaTitoli();
        return paginaProfiloUtente;
    }

    public GrigliaTitoli getGrigliaTitoli(String nomeGenere, String tipologia) {

        if (grigliaTitoli == null) {
            grigliaTitoli = new GrigliaTitoli(nomeGenere, tipologia);
        } else if(grigliaTitoli.getParent() != null){
            ((javafx.scene.layout.Pane) grigliaTitoli.getParent()).getChildren().remove(grigliaTitoli);
        }
            return grigliaTitoli;
    }

    public void invalidateGrigliaTitoli() {
        this.grigliaTitoli = null;
    }

    public void invalidaFinestre() {
        this.finestraAttuale.set("");
        this.paginaFilm = null;
        this.paginaSerieTV = null;
        this.watchlist = null;
        this.paginaProfiloUtente = null;
    }

    public void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}