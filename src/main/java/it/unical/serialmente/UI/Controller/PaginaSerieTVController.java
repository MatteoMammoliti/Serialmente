package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelSezioneSerieTv;
import it.unical.serialmente.UI.View.BannerGeneri;
import it.unical.serialmente.UI.View.BannerTitolo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaginaSerieTVController implements Initializable {

    public record TitoloData(String nome, double voto, String imageUrl) {}

    private final ModelSezioneSerieTv modelSezioneSerieTv = new ModelSezioneSerieTv();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @FXML public ScrollPane scrollPrincipale;
    @FXML public ListView<TitoloData> listNovita;
    @FXML public ListView<TitoloData> listConsigliati;
    @FXML public ListView<TitoloData> listPopolari;
    @FXML public ListView<String> listGeneri;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int dimensioneBanner = 250;
        listNovita.setPrefHeight(dimensioneBanner);
        listConsigliati.setPrefHeight(dimensioneBanner);
        listPopolari.setPrefHeight(dimensioneBanner);
        listGeneri.setPrefHeight(dimensioneBanner);

        try {
            caricaSezioneGeneri(listGeneri);
            caricaSezione(listNovita, "Novita");
            caricaSezione(listConsigliati, "Consigliati");
            caricaSezione(listPopolari, "Popolari");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void caricaSezione(ListView<TitoloData> lista, String tipologia) throws Exception {

        Task<ObservableList<TitoloData>> task = getObservableListTaskTitoli(lista, tipologia);

        lista.setCellFactory(lv -> new ListCell<>() {
            private final BannerTitolo banner = new BannerTitolo();

            @Override
            protected void updateItem(TitoloData data, boolean empty) {
                super.updateItem(data, empty);
                if (empty || data == null) {
                    setGraphic(null);
                } else {
                    banner.update(data.nome(), (int) data.voto(), data.imageUrl());
                    setGraphic(banner);
                }
            }
        });

        executor.execute(task);
    }

    private Task<ObservableList<TitoloData>> getObservableListTaskTitoli(ListView<TitoloData> lista, String tipologia) {

        Task<ObservableList<TitoloData>> task = new Task<>() {
            @Override
            protected ObservableList<TitoloData> call() throws Exception {

                List<Titolo> titoli = switch (tipologia) {
                    case "Novita"      -> modelSezioneSerieTv.getTitoliNovita();
                    case "Popolari"    -> modelSezioneSerieTv.getTitoliPopolari();
                    case "Consigliati" -> modelSezioneSerieTv.getTitoliConsigliati();
                    default            -> new ArrayList<>();
                };

                ObservableList<TitoloData> dati = FXCollections.observableArrayList();
                for (Titolo titolo : titoli) {
                    dati.add(new TitoloData(
                            titolo.getNomeTitolo(),
                            titolo.getVotoMedio(),
                            titolo.getImmagine()
                    ));
                }

                return dati;
            }
        };

        task.setOnSucceeded(e -> lista.setItems(task.getValue()));
        task.setOnFailed(e -> System.out.println("Errore durante caricamento titoli serie TV"));

        return task;
    }

    public void caricaSezioneGeneri(ListView<String> lista) throws Exception {

        Task<ObservableList<String>> task = getObservableListTaskGeneri(lista);

        lista.setCellFactory(lv -> new ListCell<>() {
            private final BannerGeneri banner = new BannerGeneri("tv");

            @Override
            protected void updateItem(String nome, boolean empty) {
                super.updateItem(nome, empty);
                if (empty || nome == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    banner.update(nome);
                    setGraphic(banner);
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
                List<Genere> generi = modelSezioneSerieTv.getGeneri();
                ObservableList<String> dati = FXCollections.observableArrayList();

                for (Genere g : generi) {
                    dati.add(g.getNomeGenere());
                }

                return dati;
            }
        };

        task.setOnSucceeded(e -> lista.setItems(task.getValue()));
        task.setOnFailed(e -> System.out.println("Errore durante caricamento generi serie TV"));

        return task;
    }
}