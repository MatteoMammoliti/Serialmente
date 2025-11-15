package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelPaginaPreferenze;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.Model.ModelSezioneFilm;
import it.unical.serialmente.UI.Model.ModelSezioneRicerca;
import it.unical.serialmente.UI.Model.ModelSezioneSerieTv;
import it.unical.serialmente.UI.View.BannerTitolo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

public class ControllerSezioneRicerca implements Initializable {

    private final ModelPaginaPreferenze modelPaginaPreferenze = new ModelPaginaPreferenze();
    private final ModelSezioneRicerca modelSezione = new ModelSezioneRicerca();
    private final Map<String, Genere> mappaGeneriDisponibili = new HashMap<>();
    private final List<Genere> listaGeneriSelezionati =  new ArrayList<>();

    @FXML private Button btnIndietro;
    @FXML private TextField campoTitolo;
    @FXML private MenuButton menuGenere;
    @FXML private TextField campoAnno;
    @FXML private RadioButton radioFilm;
    @FXML private RadioButton radioSerie;
    @FXML private Button bottoneCerca;
    @FXML private ListView<Titolo> listaRisultati;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        caricaGeneri();
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
    }

    private void caricaGeneri() {
        List<Genere> generi = modelPaginaPreferenze.getGeneri();

        menuGenere.getItems().clear();

        for (Genere g : generi) {
            CheckMenuItem cb = new CheckMenuItem(g.getNomeGenere());
            mappaGeneriDisponibili.put(g.getNomeGenere(), g);

            cb.selectedProperty().addListener((observable, oldValue, isSelected) -> {
                if(isSelected) {
                    if(!listaGeneriSelezionati.contains(g)) {
                        listaGeneriSelezionati.add(g);
                    } else {
                        listaGeneriSelezionati.remove(g);
                    }
                }
            });

            menuGenere.getItems().add(cb);
        }
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
}