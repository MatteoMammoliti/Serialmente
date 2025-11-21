package it.unical.serialmente.UI.Controller.Autenticazione;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelPaginaPreferenze;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.View.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerSceltaPreferenze implements Initializable {
    @FXML private Button procediButton;
    @FXML private Label labelErrore;
    @FXML private MenuButton elencoPiattaforme;
    @FXML private MenuButton elencoGeneri;

    private final ModelPaginaPreferenze modelPaginaPreferenze = new ModelPaginaPreferenze();
    private ViewFactory viewFactory;

    private final List<Genere> listaGeneriSelezionati =  new ArrayList<>();
    private final List<Piattaforma> listaPiattaformeSelezionate = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.viewFactory = ModelContainerView.getInstance().getViewFactory();

        caricaGeneri();
        caricaPiattaforme();

        this.procediButton.setOnAction(_ -> {
            try {
                onProcediButtonClick();
            } catch (Exception e) {
                AlertHelper.nuovoAlert(
                        "Errore",
                        Alert.AlertType.ERROR,
                        "Qualcosa è andato storto!",
                        "Errore durante il salvataggio delle preferenze. Riprovare!"
                );
                throw new RuntimeException(e);
            }
        });
    }

    private void caricaGeneri() {
        List<Genere> generi = modelPaginaPreferenze.getGeneri();

        VBox contenitore = new VBox(6);
        contenitore.setStyle("-fx-padding: 10;");

        elencoGeneri.getItems().clear();
        listaGeneriSelezionati.clear();

        for (Genere g : generi) {
            CheckBox checkBox = new CheckBox(g.getNomeGenere());
            checkBox.setStyle("-fx-text-fill: white;");

            checkBox.selectedProperty().addListener((obs, oldVal, isSelected) -> {
                if (isSelected) {
                    if (!listaGeneriSelezionati.contains(g))
                        listaGeneriSelezionati.add(g);
                } else {
                    listaGeneriSelezionati.remove(g);
                }
            });

            contenitore.getChildren().add(checkBox);
        }

        ScrollPane scroll = new ScrollPane(contenitore);
        scroll.setPrefViewportHeight(200);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        CustomMenuItem item = new CustomMenuItem(scroll);
        item.setHideOnClick(false);

        elencoGeneri.getItems().add(item);
    }

    private void caricaPiattaforme() {
        List<Piattaforma> piattaforme = modelPaginaPreferenze.getPiattaforme();

        VBox contenitore = new VBox(6);
        contenitore.setStyle("-fx-padding: 10;");

        elencoPiattaforme.getItems().clear();
        listaPiattaformeSelezionate.clear();

        for (Piattaforma p : piattaforme) {
            CheckBox checkBox = new CheckBox(p.getNomePiattaforma());
            checkBox.setStyle("-fx-text-fill: white;");

            checkBox.selectedProperty().addListener((obs, oldVal, isSelected) -> {
                if (isSelected) {
                    if (!listaPiattaformeSelezionate.contains(p))
                        listaPiattaformeSelezionate.add(p);
                } else {
                    listaPiattaformeSelezionate.remove(p);
                }
            });

            contenitore.getChildren().add(checkBox);
        }

        ScrollPane scroll = new ScrollPane(contenitore);
        scroll.setPrefViewportHeight(200);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        CustomMenuItem item = new CustomMenuItem(scroll);
        item.setHideOnClick(false);

        elencoPiattaforme.getItems().add(item);
    }

    private void onProcediButtonClick() throws Exception {
        if(this.listaGeneriSelezionati.isEmpty()) {
            this.labelErrore.setVisible(true);
            return;
        }

        modelPaginaPreferenze.aggiornaPreferenzeGeneri(this.listaGeneriSelezionati);

        if(!this.listaPiattaformeSelezionate.isEmpty()) modelPaginaPreferenze.aggiornaPreferenzePiattaforme(this.listaPiattaformeSelezionate);

        modelPaginaPreferenze.impostaPrimoAccesso(SessioneCorrente.getUtenteCorrente());

        Stage stage = (Stage)  this.procediButton.getScene().getWindow();
        stage.close();
        viewFactory.mostraPaginaFilmConMenu();
    }
}