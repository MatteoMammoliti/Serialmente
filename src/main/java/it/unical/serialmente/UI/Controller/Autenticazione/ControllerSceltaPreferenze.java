package it.unical.serialmente.UI.Controller.Autenticazione;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelPaginaPreferenze;
import it.unical.serialmente.UI.View.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private final ViewFactory viewFactory = new ViewFactory();

    private final HashMap<String, Genere> mappaGeneri = new HashMap<>();
    private final HashMap<String, Piattaforma> mappaPiattaforma = new HashMap<>();
    private final List<Genere> listaGeneriSelezionati =  new ArrayList<>();
    private final List<Piattaforma> listaPiattaformeSelezionate = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        caricaGeneri();
        caricaPiattaforme();

        this.procediButton.setOnAction(_ -> onProcediButtonClick());
    }

    private void caricaGeneri() {
        List<Genere> generi = modelPaginaPreferenze.getGeneri();

        elencoGeneri.getItems().clear();
        mappaGeneri.clear();
        listaGeneriSelezionati.clear();

        for (Genere g : generi) {
            CheckBox checkBox = new CheckBox(g.getNomeGenere());

            checkBox.selectedProperty().addListener((obs, oldVal, isSelected) -> {
                if (isSelected) {
                    if (!listaGeneriSelezionati.contains(g))
                        listaGeneriSelezionati.add(g);
                } else {
                    listaGeneriSelezionati.remove(g);
                }
            });

            CustomMenuItem item = new CustomMenuItem(checkBox);
            item.setHideOnClick(false);

            elencoGeneri.getItems().add(item);
            mappaGeneri.put(g.getNomeGenere(), g);
        }
    }

    private void caricaPiattaforme() {
        List<Piattaforma> piattaforme = modelPaginaPreferenze.getPiattaforme();

        elencoPiattaforme.getItems().clear();
        mappaPiattaforma.clear();
        listaPiattaformeSelezionate.clear();

        for (Piattaforma p : piattaforme) {
            CheckBox checkBox = new CheckBox(p.getNomePiattaforma());

            // Listener per aggiornare la lista dei selezionati
            checkBox.selectedProperty().addListener((obs, oldVal, isSelected) -> {
                if (isSelected) {
                    if (!listaPiattaformeSelezionate.contains(p))
                        listaPiattaformeSelezionate.add(p);
                } else {
                    listaPiattaformeSelezionate.remove(p);
                }
            });

            CustomMenuItem voce = new CustomMenuItem(checkBox);
            voce.setHideOnClick(false);

            elencoPiattaforme.getItems().add(voce);
            mappaPiattaforma.put(p.getNomePiattaforma(), p);
        }
    }

    private void onProcediButtonClick() {
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