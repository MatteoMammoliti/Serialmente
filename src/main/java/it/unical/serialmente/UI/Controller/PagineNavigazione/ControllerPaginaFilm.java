package it.unical.serialmente.UI.Controller.PagineNavigazione;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.TechnicalServices.Utility.ThreadPool;
import it.unical.serialmente.UI.Model.PagineNavigazione.ModelSezioneFilm;
import it.unical.serialmente.UI.View.BannerGeneri;
import it.unical.serialmente.UI.View.BannerTitolo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

public class ControllerPaginaFilm implements Initializable {

    private final ModelSezioneFilm modelSezioneFilm = new ModelSezioneFilm();
    private final ExecutorService executor = ThreadPool.get();

    public ListView<TitoloData> listConsigliati;
    public ListView <TitoloData>listPopolari;
    public ListView<String> listGeneri;
    @FXML
    public ScrollPane scrollPrincipale;

    public record TitoloData(Titolo titolo) {}
    public ListView<TitoloData> listNovita;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int dimensioneBannerini = 250;
        listNovita.setPrefHeight(dimensioneBannerini);
        listConsigliati.setPrefHeight(dimensioneBannerini);
        listPopolari.setPrefHeight(dimensioneBannerini);
        listGeneri.setPrefHeight(dimensioneBannerini);
        try {
            caricaSezioneGeneri(this.listGeneri);
            caricaSezione(this.listNovita,"Novita");
            caricaSezione(this.listConsigliati,"Consigliati");
            caricaSezione(this.listPopolari,"Popolari");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void caricaSezione(ListView<TitoloData> lista,String tipologia) throws Exception {

        Task<ObservableList<TitoloData>> task = getObservableListTaskTitoli(lista,tipologia);

        lista.setCellFactory(lv ->new ListCell<>(){
            private final BannerTitolo bannerTitolo = new BannerTitolo();
            @Override
            protected void updateItem(TitoloData data, boolean empty) {
                super.updateItem(data, empty);
                if(empty || data == null){
                    setGraphic(null);
                }else {
                    bannerTitolo.update(data.titolo);
                    setGraphic(bannerTitolo);
                }
            }
        });
        executor.execute(task);
    }

    public void caricaSezioneGeneri(ListView<String> lista) throws Exception {

        Task<ObservableList<String>> task = getObservableListTaskGeneri(lista);

        lista.setCellFactory(lv -> new ListCell<>() {
            private final BannerGeneri bannerGenere = new BannerGeneri("movie");

            @Override
            protected void updateItem(String nome, boolean empty) {
                super.updateItem(nome, empty);
                if (empty || nome == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    bannerGenere.update(nome);
                    setGraphic(bannerGenere);
                    setText(null);
                }
            }
        });
        executor.execute(task);
    }

    private Task<ObservableList<String>> getObservableListTaskGeneri(ListView<String> lista) {
        Task<ObservableList<String>> task = new Task<>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                List<Genere> generi = modelSezioneFilm.getGeneri();
                ObservableList<String> dati = FXCollections.observableArrayList();
                for (Genere genere : generi) {
                    dati.add(genere.getNomeGenere());
                }
                return dati;
            }
        };

        task.setOnSucceeded(e -> lista.setItems(task.getValue()));
        task.setOnFailed(e -> {
                    task.getException().printStackTrace();
                AlertHelper.nuovoAlert(
                        "Errore Thread",
                        Alert.AlertType.ERROR,
                        "Qualcosa è andato storto!",
                        "Qualcosa è andato storto durante il completamento di un'attività"
                );
                });
        return task;
    }

    private Task<ObservableList<TitoloData>> getObservableListTaskTitoli(ListView<TitoloData> lista,String tipologia) {
        Task<ObservableList<TitoloData>> task = new Task<>() {
            @Override
            protected ObservableList<TitoloData> call() throws Exception {
                List<Titolo> titoli = new ArrayList<>();

                titoli = switch (tipologia) {
                    case "Novita" -> modelSezioneFilm.getTitoliNovita();
                    case "Popolari" -> modelSezioneFilm.getTitoliPopolari();
                    case "Consigliati" -> modelSezioneFilm.getTitoliConsigliati();
                    default -> titoli;
                };

                ObservableList<TitoloData> dati = FXCollections.observableArrayList();
                for (Titolo titolo : titoli) {
                    dati.add(new TitoloData(titolo));
                }
                return dati;
            }
        };

        task.setOnSucceeded(e -> lista.setItems(task.getValue()));
        task.setOnFailed(e ->{
            task.getException().printStackTrace();
            AlertHelper.nuovoAlert(
                    "Errore Thread",
                    Alert.AlertType.ERROR,
                    "Qualcosa è andato storto!",
                    "Qualcosa è andato storto durante il completamento di un'attività"
            );
        });
        return task;
    }
}