package it.unical.serialmente.UI.View;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BannerinoPiattaforme extends VBox {
    public Integer bannerAltezza=170;
    public Integer bannerLarghezza=110;
    private final ImageView immagineTitolo= new ImageView();
    private String dimensionePoster = "https://image.tmdb.org/t/p/original";

    public BannerinoPiattaforme(String imgUrl) {
        this.setPrefSize(bannerLarghezza,bannerAltezza);
        this.setAlignment(Pos.TOP_CENTER);
        this.immagineTitolo.setFitWidth(bannerLarghezza);
        this.immagineTitolo.setFitHeight(bannerAltezza);
        VBox.setVgrow(this.immagineTitolo, Priority.ALWAYS);
        if(imgUrl!=null){
            Image img = new Image(dimensionePoster+imgUrl);
            this.immagineTitolo.setImage(img);
        }
        this.getChildren().addAll(this.immagineTitolo);
    }
}
