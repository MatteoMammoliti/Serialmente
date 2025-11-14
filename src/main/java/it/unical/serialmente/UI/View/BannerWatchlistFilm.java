package it.unical.serialmente.UI.View;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Objects;

public class BannerWatchlistFilm extends HBox {
    private final CacheImmagini cache=CacheImmagini.getInstance();
    private static Image PLACEHOLDER = loadPlaceholder();
    private final ImageView immagineTitolo= new ImageView();
    private final Label labelTitolo = new Label();
    private final Label labelDurataFilm = new Label();

    public BannerWatchlistFilm() {
        this.setPrefHeight(150);
        this.immagineTitolo.setFitHeight(150);
        this.immagineTitolo.setFitWidth(150);
        this.setStyle("-fx-border-color: blue; -fx-border-width: 1; -fx-border-radius: 5;");
        this.setAlignment(Pos.CENTER_LEFT);
        VBox contenitoreInfo = new VBox();
        contenitoreInfo.setAlignment(Pos.CENTER_LEFT);
        contenitoreInfo.getChildren().addAll(labelTitolo,labelDurataFilm);
        Button btnAggiungiVisionati = new Button();
        this.getChildren().addAll(immagineTitolo, contenitoreInfo, btnAggiungiVisionati);
    }

    public void update(String nome,Integer durata,String imgUrl){
        this.labelTitolo.setText(nome != null ? nome:"");
        this.labelDurataFilm.setText(durata != null ? String.valueOf(durata):"");
        this.immagineTitolo.setImage(PLACEHOLDER);
        if(imgUrl!=null && !imgUrl.isEmpty()){
            Image img = cache.getImg(imgUrl);
            this.immagineTitolo.setImage(img);
        }
    }

    private static Image loadPlaceholder() {
        if (PLACEHOLDER != null) return PLACEHOLDER;
        var url = PosterSezioneUtente.class.getResource(
                "/it/unical/serialmente/UI/Images/Generi/action.png"
        );

        if (url != null) {
            PLACEHOLDER = new Image(url.toExternalForm());
        }
        return PLACEHOLDER;
    }
}
