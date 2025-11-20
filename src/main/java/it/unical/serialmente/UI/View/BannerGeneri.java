package it.unical.serialmente.UI.View;

import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BannerGeneri extends VBox {

    private final Label nomeGenere = new Label();
    private final String tipologia;


    public BannerGeneri(String tipologia) {
        this.tipologia = tipologia;
        int altezzaBanner = 170;
        int larghezzaBanner = 200;
        this.setPrefSize(larghezzaBanner, altezzaBanner);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-border-color: #e50914; -fx-border-width: 1; -fx-border-radius: 5; -fx-font-size: 20;");
        this.getChildren().add(this.nomeGenere);
        this.setOnMouseClicked(e -> {
            GrigliaTitoli g = ModelContainerView.getInstance().getViewFactory().getGrigliaTitoli(
                    this.nomeGenere.getText(),
                    this.tipologia
            );
            ModelContainerView.getInstance().getMenuPagineController().getContenitoreView().setCenter(g);
        });
    }

    public void update(String nomeGenere){
        this.nomeGenere.setText(nomeGenere);
    }
}
