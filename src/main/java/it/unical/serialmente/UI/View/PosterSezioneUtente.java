package it.unical.serialmente.UI.View;

import it.unical.serialmente.Domain.model.Titolo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PosterSezioneUtente extends VBox {
    private static Image PLACEHOLDER = loadPlaceholder();
    private final CacheImmagini cache = CacheImmagini.getInstance();
    private final StackPane posterContainer = new StackPane();
    private final ImageView immagineTitolo= new ImageView();
    public Integer bannerAltezza=170;
    public Integer bannerLarghezza=110;
    private final Button btnPreferiti = new Button("♥");
    private Runnable rendiPreferito;
    private Runnable rimuovi;
    private final Button btnRimuovi = new Button("✕");
    private Titolo titolo;


    public PosterSezioneUtente(boolean preferito) {
        this.setPrefSize(bannerLarghezza,bannerAltezza);
        this.setAlignment(Pos.TOP_CENTER);
        this.getStyleClass().add("poster-utente-card");
        this.immagineTitolo.setFitWidth(bannerLarghezza);
        this.immagineTitolo.setFitHeight(bannerAltezza);
        this.immagineTitolo.getStyleClass().add("poster-utente-immagine");
        VBox.setVgrow(this.immagineTitolo, Priority.ALWAYS);

        btnRimuovi.getStyleClass().add("btn-rimuovi-poster");
        StackPane.setAlignment(btnRimuovi, Pos.TOP_LEFT);
        StackPane.setMargin(btnRimuovi, new Insets(5));
        btnRimuovi.setOnAction(e -> {
            if(rimuovi!=null){rimuovi.run();}
        });

        if (!preferito) {
            btnPreferiti.getStyleClass().add("btn-preferiti");
            StackPane.setAlignment(btnPreferiti, Pos.TOP_RIGHT);
            StackPane.setMargin(btnPreferiti, new Insets(5));
            btnPreferiti.setOnAction(e -> {
                if(rendiPreferito!=null){rendiPreferito.run();}
            });
        }else {
            btnPreferiti.setText("★");
            btnPreferiti.setDisable(true);
            StackPane.setAlignment(btnPreferiti, Pos.TOP_RIGHT);
            StackPane.setMargin(btnPreferiti, new Insets(5));
        }


        posterContainer.getChildren().addAll(this.immagineTitolo, btnRimuovi,btnPreferiti);
        VBox.setVgrow(posterContainer, Priority.ALWAYS);
        this.getChildren().add(posterContainer);
    }

    public void update(Titolo titolo) {
        this.immagineTitolo.setImage(PLACEHOLDER);
        this.titolo = titolo;
        if(titolo.getImmagine()!=null && !titolo.getImmagine().isEmpty()){
            Image img = cache.getImg(titolo.getImmagine());
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

    public void clickPreferito(Runnable r) {
        this.rendiPreferito = r;
    }
    public void clickElimina(Runnable r) {
        this.rimuovi = r;
    }

}