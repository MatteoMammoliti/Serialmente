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

public class ControllerInvioRisposta implements Initializable {
    public Label labelVisioneDomanda;
    public TextField textRisposta;
    public Label labelErroreRisposta;
    public Button btnAnnulla;
    public Button btnInviaRisposta;
    private final ModelCambioPassword  modelCambioPassword = new ModelCambioPassword();
    private String email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnAnnulla.setOnAction(e -> {clickAnnulla();});
        btnInviaRisposta.setOnAction(e -> {
            try {
                clickInviaRisposta();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }


    /**
     * Email passata dal controller precedente.
     */
    public void setEmail(String email){
        this.email=email;
        this.labelVisioneDomanda.setText(modelCambioPassword.getDomandaSicurezzaUtente(this.email));
    }

    /**
     * chiudo lo stage e vado nel login.
     */
    private void clickAnnulla(){
        ModelContainerView.getInstance().getViewFactory().closeStage((Stage)this.btnAnnulla.getScene().getWindow());
        ModelContainerView.getInstance().getViewFactory().mostraFinestraLogin();
    }

    /**
     * viene verificato che la risposta alla domanda di sicurezza sia quella corretta.Dopodichè viene caricata la pagina fxml
     * seleziona nuova password e passato al controller della prossima pagina l'email a cui facciamo riferimento.
     */
    private void clickInviaRisposta() throws IOException {
        if(!modelCambioPassword.controlloDomandaSicurezza(email,textRisposta.getText())){
            this.labelErroreRisposta.setVisible(true);
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/Autenticazione/RecuperoPassword/selezionaNuovaPw.fxml"));
        Parent root = fxmlLoader.load();
        ControllerImpostaNuovaPw controllerInvioRisposta = fxmlLoader.getController();
        controllerInvioRisposta.setEmail(this.email);
        Stage stage = (Stage) btnInviaRisposta.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
