package it.unical.serialmente.UI.View;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BannerinoPiattaforme extends VBox {
    public Integer bannerAltezza=170;
    public Integer bannerLarghezza=110;

    public BannerinoPiattaforme(String imgUrl) {
        this.setPrefSize(bannerLarghezza,bannerAltezza);
        this.setAlignment(Pos.TOP_CENTER);

        ImageView immagineTitolo = new ImageView();
        immagineTitolo.setFitWidth(bannerLarghezza);
        immagineTitolo.setFitHeight(bannerAltezza);

        VBox.setVgrow(immagineTitolo, Priority.ALWAYS);

        if(imgUrl!=null){
            String dimensionePoster = "https://image.tmdb.org/t/p/original";
            Image img = new Image(dimensionePoster +imgUrl);
            immagineTitolo.setImage(img);
        }

        this.getChildren().addAll(immagineTitolo);
    }
}
