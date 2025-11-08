package it.unical.serialmente.UI.View;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewFactory {

    private final StringProperty currentMenuView;
    private BorderPane dashboard;
    private BorderPane scheda;
    private BorderPane prenotazioni;
    private BorderPane myProfile;
    private SplitPane contactUs;

    public ViewFactory() {
        this.currentMenuView = new SimpleStringProperty("");
    }

    // otteniamo l'attuale view del menu (quella selezionata dal'utente)
    public StringProperty getCurrentMenuView() {
        return currentMenuView;
    }

    public void mostraFinestraRegistrazione() {

        FXMLLoader finestraRegistrazione = new FXMLLoader(getClass().getResource("/Fxml/registrazione.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(finestraRegistrazione.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Serialmente - Registrazione");
        stage.setResizable(false);
        stage.show();
    }

    // chiudiamo la finestra da cui stiamo provenendo (nella transazione Login -> Dashboard, chiudiamo la finestra di Login)
    public void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }

}