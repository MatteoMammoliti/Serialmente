package it.unical.serialmente.UI.View;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class PosterSezioneUtente extends VBox {
    private static final Image PLACEHOLDER =
            new Image(Objects.requireNonNull(
                    PosterSezioneUtente.class.getResource("/it/unical/serialmente/UI/GeneriImg/action.png")).toExternalForm()
            );
    private final CacheImmagini cache = CacheImmagini.getInstance();
    private final ImageView immagineTitolo= new ImageView();
    public Integer bannerAltezza=170;
    public Integer bannerLarghezza=110;

    public PosterSezioneUtente() {
        this.setPrefSize(bannerLarghezza,bannerAltezza);
        this.setAlignment(Pos.TOP_CENTER);
        this.immagineTitolo.setFitWidth(bannerLarghezza);
        this.immagineTitolo.setFitHeight(bannerAltezza);
        VBox.setVgrow(this.immagineTitolo, Priority.ALWAYS);
        this.setStyle("-fx-border-color: blue; -fx-border-width: 1; -fx-border-radius: 5;");
        this.getChildren().addAll(this.immagineTitolo);
    }

    public void update(String urlImg) {
        this.immagineTitolo.setImage(PLACEHOLDER);
        if(urlImg!=null && !urlImg.isEmpty()){
            Image img = cache.getImg(urlImg);
            this.immagineTitolo.setImage(img);
        }
    }


}
