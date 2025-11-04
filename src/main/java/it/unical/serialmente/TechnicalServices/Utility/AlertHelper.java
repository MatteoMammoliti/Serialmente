package it.unical.serialmente.TechnicalServices.Utility;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
            case INFORMATION -> "Images/IconeAlert/information.png";
            case WARNING -> "Images/IconeAlert/warning.png";
            case CONFIRMATION -> "Images/IconeAlert/confirmation.png";
            case ERROR -> "Images/IconeAlert/error.png";
        };

        ImageView icona = new ImageView(new Image(AlertHelper.class.getResourceAsStream(percorsoIcona)));
        alert.setGraphic(icona);
        alert.showAndWait();
    }
}