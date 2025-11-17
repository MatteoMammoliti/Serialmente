package it.unical.serialmente.UI.Controller.Autenticazione;

import it.unical.serialmente.Application.Authentication.ValidazioneRegistrazione;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelRegistrazione;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.View.ViewFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerRegistrazione implements Initializable {

    @FXML private Button loginButton;
    @FXML private Button signUpButton;
    @FXML private TextField textNome;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibile;
    @FXML private Label labelPasswordNonValida;
    @FXML private ChoiceBox sceltaDomandaSicurezza;
    @FXML private Label labelErroreDomandaSicurezza;
    @FXML private Label labelErroreRispostaSicurezza;
    @FXML private TextField textEmail;
    @FXML private Label labelErroreEmail;
    @FXML private TextField repeatPasswordVisibile;
    @FXML private PasswordField repeatPasswordField;
    @FXML private Label labelPasswordDiverse;
    @FXML private TextField textRispostaDomandaSicurezza;
    @FXML private Button eyeButton;

    private final BooleanProperty mostraPassword = new SimpleBooleanProperty(false);
    private final ModelRegistrazione modelRegistrazione = new ModelRegistrazione();
    private final ValidazioneRegistrazione validazioneRegistrazione = new ValidazioneRegistrazione();
    private final ViewFactory viewFactory = ModelContainerView.getInstance().getViewFactory();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sincronizzaPassword();
        sceltaDomandaSicurezza.getItems().addAll(
                "In che città sei nato?",
                "Nome del tuo primo animale?",
                "Titolo del primo libro letto?"
        );

        eyeButton.setOnAction(_ -> clickEyeButton());

        loginButton.setOnAction(_ -> passaAlLogin());

        signUpButton.setOnAction(_ -> {
            try {
                clickRegistraUtente();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void sincronizzaPassword(){
        passwordVisibile.textProperty().bindBidirectional(passwordField.textProperty());
        repeatPasswordVisibile.textProperty().bindBidirectional(repeatPasswordField.textProperty());

        passwordVisibile.visibleProperty().bind(mostraPassword);
        passwordVisibile.managedProperty().bind(mostraPassword);
        repeatPasswordVisibile.managedProperty().bind(mostraPassword);
        repeatPasswordVisibile.visibleProperty().bind(mostraPassword);

        passwordField.managedProperty().bind(mostraPassword.not());
        passwordField.visibleProperty().bind(mostraPassword.not());
        repeatPasswordField.managedProperty().bind(mostraPassword.not());
        repeatPasswordField.visibleProperty().bind(mostraPassword.not());
    }

    public void clickEyeButton(){
        mostraPassword.set(!mostraPassword.get());
        eyeButton.setText(mostraPassword.get() ? "🔍" : "🔒");
    }

    public void clickRegistraUtente() throws SQLException {

        if(textNome.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                repeatPasswordField.getText().isEmpty() ||
                sceltaDomandaSicurezza.getSelectionModel().isEmpty() ||
                textRispostaDomandaSicurezza.getText().isEmpty() ||
                textEmail.getText().isEmpty()
        ) {
            AlertHelper.nuovoAlert("Inserire tutti i dati richiesti",
                    Alert.AlertType.WARNING,
                    "Dati richiesti mancanti",
                    "Inserisci tutti i dati richiesti per l'iscrizione"
            );
            return;
        }

        String nome = textNome.getText();
        String email = textEmail.getText();
        String password = passwordField.getText();
        String ripetiPassword = repeatPasswordField.getText();
        String domandaSicurezza=sceltaDomandaSicurezza.getSelectionModel().getSelectedItem().toString();
        String rispostaDomandaSicurezza=textRispostaDomandaSicurezza.getText();

        if(!validazioneRegistrazione.validazioneEmail(email)){
            labelErroreEmail.setVisible(true);
            return;
        } else labelErroreEmail.setVisible(false);

        if(!validazioneRegistrazione.validazionePassword(password)){
            labelPasswordNonValida.setVisible(true);
            return;
        } else labelPasswordNonValida.setVisible(false);


        if(!password.equals(ripetiPassword)){
            labelPasswordDiverse.setVisible(true);
            return;
        } else labelPasswordDiverse.setVisible(false);

        if(sceltaDomandaSicurezza.getValue()==null){
            labelErroreDomandaSicurezza.setVisible(true);
            return;
        } else labelErroreDomandaSicurezza.setVisible(false);

        if(textRispostaDomandaSicurezza.getText()==null){
            labelErroreRispostaSicurezza.setVisible(true);
            return;
        } else  labelErroreRispostaSicurezza.setVisible(false);

        if(modelRegistrazione.registraUtente(nome,email,password,domandaSicurezza,rispostaDomandaSicurezza)){

            AlertHelper.nuovoAlert(
                    "Registrazione completata",
                    Alert.AlertType.INFORMATION,
                    "Registrazione avvenuta con successo!",
                    "Ora puoi effettuare il login."
            );

            Stage stage = (Stage) this.loginButton.getScene().getWindow();
            viewFactory.closeStage(stage);
            viewFactory.mostraFinestraLogin();
        }else  {
            AlertHelper.nuovoAlert("Qualcosa è andato storto!",
                    Alert.AlertType.WARNING,
                    "Qualcosa è andato storto!",
                    "Qualcosa è andato storto durante la registrazione."
            );
        }
    }

    public void passaAlLogin() {
        Stage stage = (Stage) this.loginButton.getScene().getWindow();
        viewFactory.closeStage(stage);
        viewFactory.mostraFinestraLogin();
    }
}
