package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.UI.Model.ModelRegistrazione;
import it.unical.serialmente.UI.Model.ModelValidazioneDatiInput;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrazioneController implements Initializable {
    public Button loginButton;
    public Button signUpButton;
    public TextField textNome;
    public PasswordField passwordField;
    public TextField passwordVisibile;
    public Label labelPasswordNonValida;
    public ChoiceBox sceltaDomandaSicurezza;
    public Label labelErroreDomandaSicurezza;
    public TextField textEmail;
    public Label labelErroreEmail;
    public TextField repeatPasswordVisibile;
    public PasswordField repeatPasswordField;
    public Label labelPasswordDiverse;
    public TextField textRispostaDomandaSicurezza;
    public Button eyeButton;

    private final BooleanProperty mostraPassword = new SimpleBooleanProperty(false);
    private final ModelRegistrazione modelRegistrazione = new ModelRegistrazione();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sincronizzaPassword();
        sceltaDomandaSicurezza.getItems().addAll(
                "In che città sei nato?",
                "Nome del tuo primo animale?",
                "Titolo del primo libro letto?"
        );

        eyeButton.setOnAction(e -> {
            clickEyeButton();

        });

        signUpButton.setOnAction(e -> {
            try {
                clickRegistraUtente();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });


    }

    @FXML
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
    @FXML
    public void clickEyeButton(){
        mostraPassword.set(!mostraPassword.get());
    }
    @FXML
    public void clickRegistraUtente() throws SQLException {
        if(textNome.getText().isEmpty() || passwordField.getText().isEmpty() || repeatPasswordField.getText().isEmpty()
        || sceltaDomandaSicurezza.getSelectionModel().isEmpty() || textRispostaDomandaSicurezza.getText().isEmpty() ||
        textEmail.getText().isEmpty()){
            System.out.println("errore");
            //AlertHelper.nuovoAlert("Inserire tutti i dati richiesti", Alert.AlertType.WARNING,"Dati richiesti mancanti","Inserisci tutti i dati richiesti per l'iscrizione");
            return;

        }
        String nome = textNome.getText();
        String email = textEmail.getText();
        String password = passwordField.getText();
        String ripetiPassword = repeatPasswordField.getText();
        String domandaSicurezza=sceltaDomandaSicurezza.getSelectionModel().getSelectedItem().toString();
        String rispostaDomandaSicurezza=textRispostaDomandaSicurezza.getText();
        if(!ModelValidazioneDatiInput.validazioneEmail(email)){
            labelErroreEmail.setVisible(true);
            System.out.println("errore email");
            return;
        }else{
            labelErroreEmail.setVisible(false);
        }
        if(!ModelValidazioneDatiInput.validazionePassword(password)){
            labelPasswordNonValida.setVisible(true);
            return;
        }
        else {
            labelPasswordNonValida.setVisible(false);
        }
        if(!password.equals(ripetiPassword)){
            labelPasswordDiverse.setVisible(true);
            return;
        }else {
            labelPasswordDiverse.setVisible(false);
        }
        if(sceltaDomandaSicurezza.getValue()==null){
            labelErroreDomandaSicurezza.setVisible(true);
            return;
        }else  {
            labelErroreDomandaSicurezza.setVisible(false);
        }
        if(modelRegistrazione.registraUtente(nome,email,password,domandaSicurezza,rispostaDomandaSicurezza)){
            System.out.println("Registrazione effettuata");
        }else  {
            System.out.println("Registrazione non effettuata");
        }





    }
}
