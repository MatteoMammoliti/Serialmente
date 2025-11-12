package it.unical.serialmente.UI.View;

import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelGrigliaTitoli;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class GrigliaTitoli extends BorderPane {

    private static final Integer NUM_TITOLI = 10;
    private ModelGrigliaTitoli modelGrigliaTitoli =  new ModelGrigliaTitoli();

    private final FlowPane griglia;
    private final Button caricaAltriTitoli;
    private String nomeGenere;
    private final List<Titolo> titoli;
    private String tipologia;
    private Integer offset;

    public GrigliaTitoli(String nomeGenere, String tipologia) {
        this.nomeGenere = nomeGenere;
        this.tipologia = tipologia;
        this.offset = 0;

        try {
            this.titoli = modelGrigliaTitoli.getTitoliPerGenere(nomeGenere, tipologia);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.griglia = new FlowPane();
        this.griglia.setHgap(15);
        this.griglia.setVgap(15);
        this.griglia.setPrefWrapLength(1100);
        caricaTitoliInGriglia();

        ScrollPane scrollPane = new ScrollPane(this.griglia);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        this.caricaAltriTitoli = new Button("Carica altri titoli");
        this.caricaAltriTitoli.setOnAction(_ -> caricaTitoliInGriglia());


        this.setCenter(scrollPane);
        this.setBottom(this.caricaAltriTitoli);
        BorderPane.setAlignment(caricaAltriTitoli, Pos.CENTER);
        BorderPane.setMargin(this.caricaAltriTitoli, new Insets(10));
    }

    private void caricaTitoliInGriglia() {
        int fine = Math.min(NUM_TITOLI + this.offset, this.titoli.size());

        for(int i = offset; i < fine; i++) {
            Titolo t = this.titoli.get(i);
            BannerTitolo bannerTitolo = new BannerTitolo();
            bannerTitolo.update(t.getNomeTitolo(),
                    (int) t.getVotoMedio(),
                    t.getImmagine());
            this.griglia.getChildren().add(bannerTitolo);
        }

        this.offset = fine;

        if(offset >= this.titoli.size()) {
            assert this.caricaAltriTitoli != null;
            this.caricaAltriTitoli.setDisable(true);
        }
    }
}