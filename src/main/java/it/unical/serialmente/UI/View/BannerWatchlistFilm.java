package it.unical.serialmente.UI.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class BannerWatchlistFilm extends HBox {


    private final ImageView imageViewPoster;
    private final Label labelNome;
    private final Label labelDurata;
    private final Button btnRimuovi;
    private final Button btnVisionato;
    private Runnable rimuovi;
    private Runnable visionato;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w92";

    public BannerWatchlistFilm() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(15.0);
        this.getStyleClass().add("card-film");
        this.setPadding(new Insets(10.0));
        this.setPrefHeight(100.0);

        imageViewPoster = new ImageView();
        imageViewPoster.setFitHeight(80.0);
        imageViewPoster.setFitWidth(60.0);
        imageViewPoster.setPreserveRatio(true);
        imageViewPoster.getStyleClass().add("poster-card");


        labelNome = new Label("Nome del Film");
        labelNome.getStyleClass().add("titolo-card");

        labelDurata = new Label("Durata: N/D");
        labelDurata.getStyleClass().add("metadati-card");

        VBox dettagliVBox = new VBox(labelNome, labelDurata);
        dettagliVBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(dettagliVBox, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnRimuovi = new Button("✕");
        btnRimuovi.getStyleClass().add("btn-rimuovi-card");
        HBox.setMargin(btnRimuovi, new Insets(0, 5.0, 0, 0));
        btnRimuovi.setOnAction(e -> {
            if(rimuovi!=null){rimuovi.run();}});


        btnVisionato = new Button("✓ Visionato");
        btnVisionato.getStyleClass().add("btn-visionato-card");
        HBox.setMargin(btnVisionato, new Insets(0, 10.0, 0, 0));
        btnVisionato.setOnAction(e -> {if(visionato!=null){visionato.run();}});

        this.getChildren().addAll(
                imageViewPoster,
                dettagliVBox,
                spacer,
                btnRimuovi,
                btnVisionato
        );
    }
    public void update(String nomeTitolo, Integer durata, String immaginePath) {
        labelNome.setText(nomeTitolo);
        labelDurata.setText("Durata: " + (durata != null ? durata.toString() + " min" : "N/D"));


        if (immaginePath != null && !immaginePath.isEmpty()) {
            String imageUrl = IMAGE_BASE_URL + immaginePath;
            imageViewPoster.setImage(new Image(imageUrl, true));
        } else {
            imageViewPoster.setImage(null);
        }
    }

    public void setRimuovi(Runnable rimuovi) {
        this.rimuovi = rimuovi;
    }
    public void setVisionato(Runnable visionato) {
        this.visionato = visionato;
    }

}