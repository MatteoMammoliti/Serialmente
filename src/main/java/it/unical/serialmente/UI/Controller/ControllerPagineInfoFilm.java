package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.Film;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.Model.ModelPagineInfoFilm;
import it.unical.serialmente.UI.View.BannerinoPiattaforme;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerPagineInfoFilm implements Initializable {
    private final ModelPagineInfoFilm modelPagineInfoFilm = new  ModelPagineInfoFilm();
    public ImageView imageViewPoster;
    public Label labelTitoloFilm;
    public Label labelAnnoVoti;
    public Label labelDurata;
    public Label labelGeneriFilm;
    public Label labelTramaFilm;
    public Button btnAggiungi;
    public HBox contenitorePiattafome;
    public Button btnIndietro;
    public HBox contenitoreInfoFilm;
    private Film titolo;
    private String dimensionePoster = "https://image.tmdb.org/t/p/original";
    boolean presenteInListe = false;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.btnAggiungi.setOnAction(e -> {
            try {
                clickAggiungi();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.btnIndietro.setOnAction(e -> {
            btnTornaIndietro();
        });
    }

    public void init(Titolo titolo) throws Exception {
        this.titolo = (Film) titolo;
        this.labelTitoloFilm.setText(this.titolo.getNomeTitolo());
        if(this.titolo.getDurataMinuti()!=null){
            this.labelDurata.setText(this.titolo.getDurataMinuti().toString());
        }
        this.labelAnnoVoti.setText(this.titolo.getAnnoPubblicazione().toString() + " - "+
                this.titolo.getVotoMedio());
        this.labelTramaFilm.setText(this.titolo.getTrama());
        this.imageViewPoster.setPreserveRatio(true);
        Image img = new Image(dimensionePoster+this.titolo.getImmagine());
        this.imageViewPoster.setImage(img);
        caricaGeneri();
        presenteInListe = modelPagineInfoFilm.controlloPresenzaTitoloWatchlist(this.titolo.getIdTitolo());
        aggiornaStatoBottone();
        setPiattaforme();
    }

    public void caricaGeneri() throws Exception {
        List<Genere> generi= titolo.getGeneriPresenti();
        for(Genere g:generi){
            System.out.println(g.getNomeGenere());
            this.labelGeneriFilm.setText(g.getNomeGenere()+" ");
        }
    }
    public void clickAggiungi() throws Exception {
        modelPagineInfoFilm.aggiungiFilmInWatchlist(this.titolo);
        presenteInListe = true;
        aggiornaStatoBottone();
    }
    private void aggiornaStatoBottone() {
        if (presenteInListe) {
            btnAggiungi.setText("Film già trackato");
            btnAggiungi.setDisable(true);
            btnAggiungi.setOpacity(0.6);
        }
    }

    private void btnTornaIndietro(){
        ModelContainerView.getInstance().getViewFactory().closeStage((Stage)this.btnIndietro.getScene().getWindow());
        ModelContainerView.getInstance().getViewFactory().mostraPaginaFilmConMenu();
    }

    private void setPiattaforme(){
        List<Piattaforma> piattaforme = this.titolo.getPiattaforme();
        for(Piattaforma p:piattaforme){
            System.out.println(p.getNomePiattaforma());
            BannerinoPiattaforme piattaforma= new BannerinoPiattaforme(p.getImgUrl());
            this.contenitorePiattafome.getChildren().add(piattaforma);
        }

    }
}
