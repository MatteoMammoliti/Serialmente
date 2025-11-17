package it.unical.serialmente.UI.Controller.PagineNavigazione;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelPaginaPreferenze;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.Model.PagineNavigazione.ModelSezioneRicerca;
import it.unical.serialmente.UI.View.BannerTitolo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class ControllerSezioneRicerca implements Initializable {

    private final ModelPaginaPreferenze modelPaginaPreferenze = new ModelPaginaPreferenze();
    private final ModelSezioneRicerca modelSezione = new ModelSezioneRicerca();
    private final List<Genere> listaGeneriSelezionati =  new ArrayList<>();
    private final List<CheckBox> listaCheckBoxSelezionati = new ArrayList<>();

    @FXML private Label labelVuoto;
    @FXML private Button btnIndietro;
    @FXML private TextField campoTitolo;
    @FXML private MenuButton menuGenere;
    @FXML private TextField campoAnno;
    @FXML private RadioButton radioFilm;
    @FXML private RadioButton radioSerie;
    @FXML private Button bottoneCerca;
    @FXML private ListView<Titolo> listaRisultati;
    @FXML private ToggleGroup toggleTipo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        caricaGeneri();

        toggleTipo = new ToggleGroup();
        radioFilm.setToggleGroup(toggleTipo);
        radioSerie.setToggleGroup(toggleTipo);

        try {
            caricaTitoli(modelSezione.getTitoli());

            listaRisultati.setCellFactory(titoloListView -> new ListCell<Titolo>() {
                private final BannerTitolo bannerTitolo = new BannerTitolo();
                protected void updateItem(Titolo item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        bannerTitolo.update(item);
                        setGraphic(bannerTitolo);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.btnIndietro.setOnAction(event -> {
            ModelContainerView.getInstance().getViewFactory().tornaAllaPaginaPrecedente();
        });

        this.bottoneCerca.setOnAction(event -> {
            try {
                iniziaRicerca();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void caricaGeneri() {
        List<Genere> generi = modelPaginaPreferenze.getGeneri();

        VBox contenitore = new VBox(6);
        contenitore.setStyle("-fx-padding: 10;");

        menuGenere.getItems().clear();

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
            listaCheckBoxSelezionati.add(checkBox);
        }

        ScrollPane scroll = new ScrollPane(contenitore);
        scroll.setPrefViewportHeight(200);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        CustomMenuItem item = new CustomMenuItem(scroll);
        item.setHideOnClick(false);

        menuGenere.getItems().add(item);
    }

    private void caricaTitoli(List<Titolo> titoli) {
        Task<ObservableList<Titolo>> task = new Task<>() {
            @Override
            protected ObservableList<Titolo> call() throws Exception {
                return  FXCollections.observableArrayList(titoli);
            }
        };

        task.setOnSucceeded(event -> {
            listaRisultati.setItems((ObservableList<Titolo>) task.getValue());
        });

        task.setOnFailed(event -> {
            task.getException().printStackTrace();
        });
        new Thread(task).start();
    }

    private void iniziaRicerca() throws Exception {

        this.labelVuoto.setVisible(false);

        if(!radioFilm.isSelected() &&
            !radioSerie.isSelected() &&
            this.listaGeneriSelezionati.isEmpty() &&
            this.campoTitolo.getText().isBlank() &&
            this.campoAnno.getText().isBlank()
        ) {
            caricaTitoli(modelSezione.getTitoli());
            azzeraCampi();
            return;
        }

        String tipologia;

        if(radioFilm.isSelected()) {
            tipologia = "movie";
        } else if(radioSerie.isSelected()) {
            tipologia = "tv";
        } else tipologia = null;


        List<Titolo> t = modelSezione.getTitoliRicerca(
                !this.campoTitolo.getText().isBlank() ? this.campoTitolo.getText().toLowerCase() : null,
                tipologia,
                !this.listaGeneriSelezionati.isEmpty() ? this.listaGeneriSelezionati : null,
                !this.campoAnno.getText().isBlank() ? Integer.parseInt(this.campoAnno.getText()) : null
        );

        if(t.isEmpty()) {
            listaRisultati.setItems(FXCollections.observableArrayList());
            this.labelVuoto.setVisible(true);
        } else caricaTitoli(t);
        azzeraCampi();
    }

    private void azzeraCampi() {
        this.campoTitolo.clear();
        this.campoAnno.clear();
        toggleTipo.selectToggle(null);
        this.listaGeneriSelezionati.clear();

        for(CheckBox cb : listaCheckBoxSelezionati) {
            cb.setSelected(false);
        }

        this.listaCheckBoxSelezionati.clear();
    }
}