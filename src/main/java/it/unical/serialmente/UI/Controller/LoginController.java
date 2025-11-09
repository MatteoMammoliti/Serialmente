package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Utente;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.UI.Model.ModelLogin;
import it.unical.serialmente.UI.Model.ModelView;
import it.unical.serialmente.UI.View.ViewFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Button loginButton;
    @FXML private Label labelLoginError;
    @FXML private TextField textEmail;
    @FXML private Button signupButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField showPassword;
    @FXML private Button eyeButton;

    private final ViewFactory viewFactory = ModelView.getInstance().getViewFactory();
    private final ModelLogin modelLogin = new ModelLogin();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sincronizzaPassword();

        signupButton.setOnAction(e -> passaAllaRegistrazione());

        loginButton.setOnAction(e -> login());

        eyeButton.setOnAction(e -> nascondiPassword());
    }

    public void passaAllaRegistrazione() {
        Stage stage = (Stage) this.signupButton.getScene().getWindow();
        viewFactory.closeStage(stage);
        viewFactory.mostraFinestraRegistrazione();
    }

    public void sincronizzaPassword(){
        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (!showPassword.isVisible()) {
                showPassword.setText(newText);
            }
        });
        showPassword.textProperty().addListener((obs, oldText, newText) -> {
            if (showPassword.isVisible()) {
                passwordField.setText(newText);
            }
        });
    }

    public void login() {

        if(textEmail.getText().isEmpty() ||
        passwordField.getText().isEmpty()
        ){
            AlertHelper.nuovoAlert("Inserire tutti i dati richiesti",
                    Alert.AlertType.WARNING,
                    "Dati richiesti mancanti",
                    "Inserisci tutti i dati richiesti per l'autenticazione"
            );
        }

        String email = textEmail.getText();
        String password = passwordField.getText();

        Utente utenteAutenticato = modelLogin.autenticazioneUtente(email, password);
        if(utenteAutenticato != null) {

            Stage stage = (Stage) loginButton.getScene().getWindow();
            viewFactory.closeStage(stage);
            viewFactory.mostraHomePageConMenu();
            SessioneCorrente.setUtenteCorrente(utenteAutenticato);

        }
        else labelLoginError.setVisible(true);
    }

    private void nascondiPassword() {
        boolean isVisible = showPassword.isVisible();

        showPassword.setVisible(!isVisible);
        showPassword.setManaged(!isVisible);
        passwordField.setVisible(isVisible);
        passwordField.setManaged(isVisible);

        showPassword.setText(passwordField.getText());

        eyeButton.setText(isVisible ? "🔍" : "🔒");
    }
}