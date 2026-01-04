package it.unical.serialmente.UI.Controller.Autenticazione.CambioPW;

import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelCambioPassword;
import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCambioPw implements Initializable {
    public TextField textEmail;
    public Label labelErroreEmail;
    
    private final ModelCambioPassword modelCambioPassword = new ModelCambioPassword();
    public Button btnAnnulla;
    public Button btnInvia;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnInvia.setOnAction(e -> {
            try {
                clickInvia();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnAnnulla.setOnAction(e -> {
            clickAnnulla();
        });

    }

    /**
     * La funzione verifica che l'email utilizzata esista e sia associata ad un utente.Dopodichè, passa l'email dentro il textEmail
     * al controller della nuova scena che andrà a caricare (Risposta domanda sicurezza).
     */
    private void clickInvia() throws IOException {
        if(!modelCambioPassword.controlloEmailEsistente(textEmail.getText())){
            labelErroreEmail.setVisible(true);
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/Autenticazione/RecuperoPassword/rispostaDomandaSicurezza.fxml"));
        Parent root = fxmlLoader.load();
        ControllerInvioRisposta controllerInvioRisposta = fxmlLoader.getController();
        controllerInvioRisposta.setEmail(textEmail.getText());
        Stage stage = (Stage) btnInvia.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    /**
     * Chiudo questo stage e vado nel login.
     */
    private void clickAnnulla(){
        ModelContainerView.getInstance().getViewFactory().closeStage((Stage)this.btnAnnulla.getScene().getWindow());
        ModelContainerView.getInstance().getViewFactory().mostraFinestraLogin();
    }

}
