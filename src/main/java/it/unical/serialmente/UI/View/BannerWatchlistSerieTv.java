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

public class BannerWatchlistSerieTv extends HBox {

    private final ImageView imageViewPoster;
    private final Label labelNome;
    private final Label labelStagioneEpisodio;
    private final Button btnRimuovi;
    private final Button btnSegnaEpisodio;
    private final Button btnVisionatoSerie;
    private Runnable rimuovi;
    private Runnable segnaEpisodio;
    private Runnable visionatoSerie;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w92";

    public BannerWatchlistSerieTv() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(15.0);
        this.getStyleClass().add("card-serie");
        this.setPadding(new Insets(10.0));
        this.setPrefHeight(100.0);

        imageViewPoster = new ImageView();
        imageViewPoster.setFitHeight(80.0);
        imageViewPoster.setFitWidth(60.0);
        imageViewPoster.setPreserveRatio(true);
        imageViewPoster.getStyleClass().add("poster-card");

        labelNome = new Label();
        labelNome.getStyleClass().add("titolo-card");

        labelStagioneEpisodio = new Label();
        labelStagioneEpisodio.getStyleClass().add("metadati-card-serie");

        VBox dettagliVBox = new VBox(labelNome, labelStagioneEpisodio);
        dettagliVBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(dettagliVBox, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnRimuovi = new Button("✕");
        btnRimuovi.getStyleClass().add("btn-rimuovi-card");
        HBox.setMargin(btnRimuovi, new Insets(0, 5.0, 0, 0));
        btnRimuovi.setOnAction(e -> {if(rimuovi!=null){rimuovi.run();}});


        btnSegnaEpisodio = new Button("▶ Episodio Visto");
        btnSegnaEpisodio.getStyleClass().add("btn-episodio-card");
        HBox.setMargin(btnSegnaEpisodio, new Insets(0, 10.0, 0, 0));
        btnSegnaEpisodio.setOnAction(event->{if(segnaEpisodio!=null){segnaEpisodio.run();}});

        btnVisionatoSerie = new Button("✓ Serie Vista");
        btnVisionatoSerie.getStyleClass().add("btn-visionato-card-alt");
        HBox.setMargin(btnVisionatoSerie, new Insets(0, 10.0, 0, 0));
        btnVisionatoSerie.setOnAction(event -> {if(visionatoSerie!=null){visionatoSerie.run();}});

        this.getChildren().addAll(
                imageViewPoster,
                dettagliVBox,
                spacer,
                btnRimuovi,
                btnSegnaEpisodio,
                btnVisionatoSerie
        );
    }

    public void update(String nomeTitolo, Integer numStagione, Integer numEpisodio, String nomeEpisodio, String immaginePath) {
        labelNome.setText(nomeTitolo);

        String episodioCorrente = String.format("S%02d E%02d - %s",
                numStagione,
                numEpisodio,
                (nomeEpisodio != null ? nomeEpisodio : "N/D"));
        labelStagioneEpisodio.setText(episodioCorrente);
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
    public void setSegnaEpisodio(Runnable segnaEpisodio) {
        this.segnaEpisodio = segnaEpisodio;
    }
    public void setVisionatoSerie(Runnable visionatoSerie) {
        this.visionatoSerie = visionatoSerie;
    }

}