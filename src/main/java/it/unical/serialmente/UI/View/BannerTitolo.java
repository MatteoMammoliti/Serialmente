package it.unical.serialmente.UI.View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class BannerTitolo extends VBox {
    private final ImageView immagineTitolo= new ImageView();
    private final Label nomeTitolo = new Label();
    private final Label votoTitolo = new Label();
    public Integer bannerAltezza=250;
    public Integer bannerLarghezza=130;

    public BannerTitolo() {
        this.setPrefSize(bannerLarghezza,bannerAltezza);
        this.setAlignment(Pos.CENTER);
        immagineTitolo.setPreserveRatio(true);
        this.immagineTitolo.fitWidthProperty().bind(this.widthProperty().multiply(0.8));
        VBox.setVgrow(this.immagineTitolo, Priority.ALWAYS);
        this.setStyle("-fx-border-color: blue; -fx-border-width: 1; -fx-border-radius: 5;");
        this.getChildren().addAll(this.immagineTitolo,this.nomeTitolo,this.votoTitolo);
    }

    public void update(String nomeTitolo,Integer votoTitolo,String urlImg) {
        this.nomeTitolo.setText(nomeTitolo != null ? nomeTitolo : "");
        this.votoTitolo.setText(votoTitolo.toString() !=null ? votoTitolo.toString() : "");
        if(urlImg!=null && !urlImg.equals("")){
            Image img = new Image(BannerTitolo.class.getResourceAsStream(urlImg));
            this.immagineTitolo.setImage(img);
        }



    }


}
