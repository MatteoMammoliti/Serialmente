package it.unical.serialmente.UI.View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BannerGeneri extends VBox {

    private final Label nomeGenere = new Label();
    private Integer altezzaBanner = 170;
    private Integer larghezzaBanner = 200;

    public BannerGeneri() {
        this.setPrefSize(larghezzaBanner, altezzaBanner);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-border-color: blue; -fx-border-width: 1; -fx-border-radius: 5; -fx-font-size: 20;");
        this.getChildren().add(this.nomeGenere);
    }

    public void update(String nomeGenere){
        this.nomeGenere.setText(nomeGenere);
    }
}
