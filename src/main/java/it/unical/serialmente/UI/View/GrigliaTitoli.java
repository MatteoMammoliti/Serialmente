package it.unical.serialmente.UI.View;

import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.UI.Model.ModelGrigliaTitoli;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class GrigliaTitoli extends BorderPane {

    private static final Integer NUM_TITOLI = 10;
    private final ModelGrigliaTitoli modelGrigliaTitoli = new ModelGrigliaTitoli();

    private final FlowPane griglia;
    private final Button caricaAltriTitoli;

    private List<Titolo> titoli;
    private int offset = 0;

    public GrigliaTitoli(String nomeGenere, String tipologia) {

        this.griglia = new FlowPane();
        griglia.setHgap(15);
        griglia.setVgap(15);
        griglia.setPrefWrapLength(1100);

        ScrollPane scrollPane = new ScrollPane(griglia);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        this.caricaAltriTitoli = new Button("Carica altri titoli");
        this.caricaAltriTitoli.setDisable(true);

        this.caricaAltriTitoli.setOnAction(e -> caricaTitoliInGriglia());

        this.setCenter(scrollPane);
        this.setBottom(caricaAltriTitoli);
        BorderPane.setAlignment(caricaAltriTitoli, Pos.CENTER);
        BorderPane.setMargin(caricaAltriTitoli, new Insets(10));

        caricaTitoli(nomeGenere, tipologia);
    }

    private void caricaTitoli(String nomeGenere, String tipologia) {

        Task<List<Titolo>> task = new Task<>() {
            @Override
            protected List<Titolo> call() throws Exception {
                return modelGrigliaTitoli.getTitoliPerGenere(nomeGenere, tipologia, 1).join();
            }
        };

        task.setOnSucceeded(e -> {
            this.titoli = task.getValue();
            this.caricaAltriTitoli.setDisable(false);
            caricaTitoliInGriglia();

            try {
                modelGrigliaTitoli.getTitoliPerGenere(nomeGenere, tipologia, 2).thenAccept(secondaPagina -> {
                    Platform.runLater(() -> {
                        this.titoli.addAll(secondaPagina);
                    });
                });
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        task.setOnFailed(e -> {
            AlertHelper.nuovoAlert(
                    "Errore Thread",
                    Alert.AlertType.ERROR,
                    "Qualcosa è andato storto!",
                    "Qualcosa è andato storto durante il completamento di un'attività"
            );
        });

        new Thread(task).start();
    }

    private void caricaTitoliInGriglia() {
        if (titoli == null) return;

        int fine = Math.min(offset + NUM_TITOLI, titoli.size());

        for (int i = offset; i < fine; i++) {
            Titolo t = titoli.get(i);

            BannerTitolo banner = new BannerTitolo();
            banner.update(t);

            griglia.getChildren().add(banner);
        }

        offset = fine;

        if (offset >= titoli.size()) {
            caricaAltriTitoli.setDisable(true);
        }
    }
}