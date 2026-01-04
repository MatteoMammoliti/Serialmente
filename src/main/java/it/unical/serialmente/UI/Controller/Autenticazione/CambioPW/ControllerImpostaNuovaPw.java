package it.unical.serialmente.UI.Controller.Autenticazione.CambioPW;

import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelCambioPassword;
import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerImpostaNuovaPw implements Initializable {
    public PasswordField passwordField;
    public TextField passwordVisibile;
    public PasswordField passwordFieldRipeti;
    public TextField passwordVisibileRipeti;
    public Button eyeButton;
    public Label labelPasswordNonUguali;
    public Label labelPasswordNonConforme;
    public Button btnAnnulla;
    public Button btnInviaRisposta;
    private String email;
    private final BooleanProperty mostraPassword = new SimpleBooleanProperty(false);
    private final ModelCambioPassword modelCambioPassword = new ModelCambioPassword();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sincronizzaPassword();
        eyeButton.setOnAction(e -> {clickEyeButton();});
        btnAnnulla.setOnAction(e -> {clickAnnulla();});
        btnInviaRisposta.setOnAction(e -> {clickInvia();});

    }

    /**
     * Email passata dal controller precedente.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sincronizza il testo nei Fild password visibili e non visibili attravarso BindBidirectional e imposta quele coppia
     * deve essere visibile confrontanto il valore booleano di mostraPassword, essendo essa un booleano osservabile.
     */
    public void sincronizzaPassword(){
        passwordVisibile.textProperty().bindBidirectional(passwordField.textProperty());
        passwordVisibileRipeti.textProperty().bindBidirectional(passwordFieldRipeti.textProperty());

        passwordVisibile.visibleProperty().bind(mostraPassword);
        passwordVisibile.managedProperty().bind(mostraPassword);
        passwordFieldRipeti.managedProperty().bind(mostraPassword);
        passwordVisibileRipeti.visibleProperty().bind(mostraPassword);

        passwordField.managedProperty().bind(mostraPassword.not());
        passwordField.visibleProperty().bind(mostraPassword.not());
        passwordFieldRipeti.managedProperty().bind(mostraPassword.not());
        passwordFieldRipeti.visibleProperty().bind(mostraPassword.not());
    }
    public void clickEyeButton(){
        mostraPassword.set(!mostraPassword.get());
        eyeButton.setText(mostraPassword.get() ? "🔍" : "🔒");
    }

    /**
     * Chiude questo stage e apro il login.
     */
    private void clickAnnulla(){
        ModelContainerView.getInstance().getViewFactory().closeStage((Stage)this.btnAnnulla.getScene().getWindow());
        ModelContainerView.getInstance().getViewFactory().mostraFinestraLogin();
    }
    private boolean confrontoPassword(){
        return this.passwordField.getText().equals(passwordFieldRipeti.getText());
    }

    private void clickInvia(){
        this.labelPasswordNonUguali.setVisible(false);
        this.labelPasswordNonConforme.setVisible(false);
        if(!confrontoPassword()){
            this.labelPasswordNonUguali.setVisible(true);
            return;
        }
        if(!modelCambioPassword.verificaIdonietaPassword(this.passwordField.getText())){
            this.labelPasswordNonConforme.setVisible(true);
            return;
        }
        if(modelCambioPassword.cambiaPassword(this.email,passwordField.getText())){

            AlertHelper.nuovoAlert(
                    "Registrazione completata",
                    Alert.AlertType.INFORMATION,
                    "Cambio password avvenuto con successo!",
                    "Ora puoi effettuare il login con le nuove credenziali."
            );

            ModelContainerView.getInstance().getViewFactory().closeStage((Stage)this.btnInviaRisposta.getScene().getWindow());
            ModelContainerView.getInstance().getViewFactory().mostraFinestraLogin();
        }
    }
}
