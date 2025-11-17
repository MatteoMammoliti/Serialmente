package it.unical.serialmente.TechnicalServices.Utility;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class AlertHelper {
    /**
     * Funzione di utility che crea al volo un alert con i parametri passati
     *
     * @param titolo
     * @param tipologia
     * @param testoHeader
     * @param testoMessaggio
     */
    public static void nuovoAlert(String titolo,  Alert.AlertType tipologia, String testoHeader, String testoMessaggio) {
        Alert alert = new Alert(tipologia);
        alert.setTitle(titolo);
        alert.setHeaderText(testoHeader);
        alert.setContentText(testoMessaggio);

        String percorsoIcona = switch (tipologia) {
            case NONE -> null;
            case INFORMATION -> "/it/unical/serialmente/UI/IconeAlert/information.png";
            case WARNING -> "/it/unical/serialmente/UI/Images/IconeAlert/warning.png";
            case CONFIRMATION -> "/it/unical/serialmente/UI/Images/IconeAlert/confirmation.png";
            case ERROR -> "/it/unical/serialmente/UI/Images/IconeAlert/error.png";
        };

        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(
                Objects.requireNonNull(AlertHelper.class.getResource("/it/unical/serialmente/UI/CSS/alert.css")).toExternalForm()
        );
        pane.getStyleClass().add("custom-dialog");

        ImageView icona = new ImageView(new Image(AlertHelper.class.getResourceAsStream(percorsoIcona)));
        alert.setGraphic(icona);
        alert.showAndWait();
    }
}