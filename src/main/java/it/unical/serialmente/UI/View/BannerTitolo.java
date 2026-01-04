package it.unical.serialmente.UI.View;

import it.unical.serialmente.Application.Service.TitoloService;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Controller.ControllerPagineInfoFilm;
import it.unical.serialmente.UI.Controller.ControllerPagineInfoSerieTv;
import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BannerTitolo extends VBox {
    private static final Image PLACEHOLDER =
            new Image(Objects.requireNonNull(
                    BannerTitolo.class.getResource("/it/unical/serialmente/UI/Images/Generi/action.png")).toExternalForm()
            );

    private final TitoloService titoloService = new TitoloService();
    private final CacheImmagini cache = CacheImmagini.getInstance();
    private final ImageView immagineTitolo= new ImageView();
    private final Label nomeTitolo = new Label();
    private final Label votoTitolo = new Label();
    public Integer bannerAltezza = 170;
    public Integer bannerLarghezza = 110;
    private Titolo titolo;

    public BannerTitolo() {
        this.setPrefSize(bannerLarghezza,bannerAltezza);
        this.setAlignment(Pos.TOP_CENTER);
        this.immagineTitolo.setFitWidth(bannerLarghezza);
        this.immagineTitolo.setFitHeight(bannerAltezza);
        VBox.setVgrow(this.immagineTitolo, Priority.ALWAYS);
        this.getStyleClass().add("banner-poster");
        this.getChildren().addAll(this.immagineTitolo,this.nomeTitolo,this.votoTitolo);
        this.setOnMouseClicked(event -> {
            try {
                apriPaginaInfo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void update(Titolo titolo) {
        this.titolo = titolo;
        this.nomeTitolo.setText(titolo.getNomeTitolo() != null ? titolo.getNomeTitolo() : "");
        int voto = (int) titolo.getVotoMedio();
        this.votoTitolo.setText("Voto: "+voto);
        this.immagineTitolo.setImage(PLACEHOLDER);
        if(titolo.getImmagine()!=null && !titolo.getImmagine().isEmpty()){
            Image img = cache.getImg(titolo.getImmagine());
            this.immagineTitolo.setImage(img);
        }
    }

    public void apriPaginaInfo() throws Exception {

        Stage stage = (Stage) this.getScene().getWindow();
        Parent paginaCorrente = stage.getScene().getRoot();

        ModelContainerView.getInstance()
                .getViewFactory()
                .setPaginaPrecedente(paginaCorrente, null);

        if(this.titolo.getTipologia().equals("Film")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/paginaInfoFilm.fxml"));
            Parent root = loader.load();
            ControllerPagineInfoFilm controller = loader.getController();
            controller.init(this.titolo);

            CompletableFuture.supplyAsync(() -> {
                try {
                    return titoloService.setDati(this.titolo);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }})
                .thenAcceptAsync(titoloCompleto -> {
                    Platform.runLater(() -> controller.initDatiCompleti(titoloCompleto));
                }
            );

            Stage stageLocale = (Stage) this.getScene().getWindow();
            stageLocale.getScene().setRoot(root);
        }
        else  if(this.titolo.getTipologia().equals("SerieTv")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/paginaInfoSerie.fxml"));
            Parent root = loader.load();
            ControllerPagineInfoSerieTv controller = loader.getController();
            controller.init(this.titolo);

            CompletableFuture.supplyAsync(() -> {
                        try {
                            return titoloService.setDati(this.titolo);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }})
                    .thenAcceptAsync(titoloCompleto -> {
                                Platform.runLater(() -> controller.initDatiCompleti(titoloCompleto));
                            }
                    );

            Stage stageLocale = (Stage) this.getScene().getWindow();
            stageLocale.getScene().setRoot(root);
        }
    }
}