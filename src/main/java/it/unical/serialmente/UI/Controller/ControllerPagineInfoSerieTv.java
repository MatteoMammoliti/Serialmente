package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.Model.ModelPagineInfoSerieTv;
import it.unical.serialmente.UI.View.BannerinoPiattaforme;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerPagineInfoSerieTv implements Initializable {
    public Button btnIndietro;
    public HBox contenitoreInfoSerie;
    public ImageView imageViewPoster;
    public Label labelTitoloSerie;
    public Label labelStagioniEpisodi;
    public Label labelVoto;
    public Label labelGeneriSerie;
    public Label labelTramaSerie;
    public Button btnAggiungi;
    public HBox contenitorePiattafome;
    public Accordion accordionStagioni;

    private SerieTV serie;
    private final ModelPagineInfoSerieTv modelPagineInfoSerieTv = new ModelPagineInfoSerieTv();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnAggiungi.setOnAction(e -> {
            try {
                aggiungiSerieTvInWatchlist(this.serie);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnIndietro.setOnAction(e -> {
            tornaIndietro();
        });
    }

    public void init(Titolo titolo) throws Exception {
        SerieTV serie=(SerieTV) titolo;
        this.serie=serie;
        this.labelTitoloSerie.setText(this.serie.getNomeTitolo());
        this.labelVoto.setText(String.valueOf(this.serie.getVotoMedio()));
        this.labelTramaSerie.setText(this.serie.getTrama());
        caricaStagioni(this.serie.getStagioni());
        aggiornaBottone();
        caricaPiattaforme(serie.getPiattaforme());
        caricaGeneri(modelPagineInfoSerieTv.getGeneriSerie(this.serie.getIdTitolo()));


    }

    private void caricaGeneri(List<Genere> generi){
        for(Genere g:generi){
            this.labelGeneriSerie.setText(g.getNomeGenere()+" ");
        }
    }
    private void caricaStagioni(List<Stagione> stagioni){
        for(int i=0;i<stagioni.size();i++){
            TitledPane pane= new TitledPane();
            pane.setText(stagioni.get(i).getNomeStagione()+"("+stagioni.get(i).getEpisodi().size()+")");
            pane.getStyleClass().add("titled-pane-stagione");
            VBox contenitoreEpisodi= new VBox();
            contenitoreEpisodi.getStyleClass().add("contenitore-episodi");
            popolaEpisodi(contenitoreEpisodi,stagioni.get(i).getEpisodi(),i+1);
            pane.setContent(contenitoreEpisodi);
            accordionStagioni.getPanes().add(pane);
        }

    }

    private void popolaEpisodi(VBox contenitore,List<Episodio> episodi,Integer index){
        for (int i=0;i<episodi.size();i++) {
            HBox episodioCard = new HBox(10);
            episodioCard.getStyleClass().add("card-episodio");
            episodioCard.setPadding(new javafx.geometry.Insets(10));
            Label numeroLabel = new Label(
                    String.format("Stagione: "+index+ "Episodio: "+ i)
            );
            numeroLabel.getStyleClass().add("numero-episodio");

            Label durataLabel = new Label(episodi.get(i).getDurataEpisodio().toString());
            durataLabel.getStyleClass().add("durata-episodio");
            episodioCard.getChildren().addAll(numeroLabel, durataLabel);
            contenitore.getChildren().add(episodioCard);
        }
    }

    private void aggiornaBottone(){
        boolean presenteInLista = false;
        if(presenteInLista){
            this.btnAggiungi.setDisable(true);
            this.btnAggiungi.setOpacity(0.6);
            this.btnAggiungi.setText("Serie già trackata");
        }else{
            this.btnAggiungi.setDisable(false);
            this.btnAggiungi.setOpacity(1);
            this.btnAggiungi.setText("Aggiungi in Watchlist");
        }

    }
    private void aggiungiSerieTvInWatchlist(SerieTV serie) throws SQLException {
        modelPagineInfoSerieTv.aggiungiSerieTvInWatchlist(serie);
        aggiornaBottone();
    }

    private void tornaIndietro(){
        ModelContainerView.getInstance().getViewFactory().closeStage((Stage)this.btnIndietro.getScene().getWindow());
        ModelContainerView.getInstance().getViewFactory().mostraPaginaFilmConMenu();
    }

    private void caricaPiattaforme(List<Piattaforma> piattaforme) throws SQLException {
        for(Piattaforma p:piattaforme){
            BannerinoPiattaforme banner = new BannerinoPiattaforme(p.getImgUrl());
            this.contenitorePiattafome.getChildren().add(banner);
        }
    }
}
