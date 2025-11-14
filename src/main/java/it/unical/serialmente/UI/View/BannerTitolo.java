package it.unical.serialmente.UI.View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Objects;

public class BannerTitolo extends VBox {
    private static final Image PLACEHOLDER =
            new Image(Objects.requireNonNull(
                    BannerTitolo.class.getResource("/it/unical/serialmente/UI/Images/Generi/action.png")).toExternalForm()
            );

    private final CacheImmagini cache = CacheImmagini.getInstance();
    private final ImageView immagineTitolo= new ImageView();
    private final Label nomeTitolo = new Label();
    private final Label votoTitolo = new Label();
    public Integer bannerAltezza=170;
    public Integer bannerLarghezza=110;

    public BannerTitolo() {
        this.setPrefSize(bannerLarghezza,bannerAltezza);
        this.setAlignment(Pos.TOP_CENTER);
        this.immagineTitolo.setFitWidth(bannerLarghezza);
        this.immagineTitolo.setFitHeight(bannerAltezza);
        VBox.setVgrow(this.immagineTitolo, Priority.ALWAYS);
        this.getStyleClass().add("banner-poster");
        this.getChildren().addAll(this.immagineTitolo,this.nomeTitolo,this.votoTitolo);
    }

    public void update(String nomeTitolo,Integer votoTitolo,String urlImg) {
        this.nomeTitolo.setText(nomeTitolo != null ? nomeTitolo : "");
        this.votoTitolo.setText(votoTitolo.toString());
        this.immagineTitolo.setImage(PLACEHOLDER);
        if(urlImg!=null && !urlImg.isEmpty()){
            Image img = cache.getImg(urlImg);
            this.immagineTitolo.setImage(img);
        }
    }
}
