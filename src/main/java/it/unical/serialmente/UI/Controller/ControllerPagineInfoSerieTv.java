package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.*;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.Model.ModelPagineInfoSerieTv;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    public Accordion accordionStagioni;
    public Label labelPiattaforme;

    private SerieTV serie;
    private final ModelPagineInfoSerieTv modelPagineInfoSerieTv = new ModelPagineInfoSerieTv();
    private boolean presenteInLista;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.btnAggiungi.setDisable(true);
        btnAggiungi.setOnAction(e -> {
            try {
                aggiungiSerieTvInWatchlist(this.serie);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnIndietro.setOnAction(e -> {
            tornaIndietro();
        });
    }

    public void init(Titolo titolo) throws Exception {
        this.serie= (SerieTV) titolo;
        this.labelTitoloSerie.setText(this.serie.getNomeTitolo());
        this.labelVoto.setText("Voto medio: "+this.serie.getVotoMedio());
        this.labelTramaSerie.setText(this.serie.getTrama());
        String dimensionePoster = "https://image.tmdb.org/t/p/original";
        this.imageViewPoster.setImage(new Image(dimensionePoster +this.serie.getImmagine()));
        presenteInLista = modelPagineInfoSerieTv.controlloSeSerieInListe(this.serie.getIdTitolo());
    }


    public void initDatiCompleti(Titolo t) {
        this.serie = (SerieTV) t;
        caricaGeneri();
        caricaPiattaforme(this.serie.getPiattaforme());
        caricaStagioni(this.serie.getStagioni());
        calcolaEpisodiTotali(this.serie.getStagioni());
        aggiornaBottone();
    }

    public void caricaGeneri() {
        List<Genere> generi = this.serie.getGeneriPresenti();
        StringBuilder sb = new StringBuilder();

        for (Genere g : generi) {
            sb.append(g.getNomeGenere()).append(" ");
        }

        labelGeneriSerie.setText(sb.toString());
    }

    private void calcolaEpisodiTotali(List<Stagione> stagioni){
        int totale=0;
        for(Stagione stagione:stagioni){
            totale+=stagione.getEpisodi().size();
        }
        labelStagioniEpisodi.setText(stagioni.size() + " Stagioni | Episodi " +totale);
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
            int numeroEp= i +1;
            Label numeroLabel = new Label( "Episodio: "+ numeroEp );
            numeroLabel.getStyleClass().add("numero-episodio");

            Label durataLabel = new Label("Durata: " + episodi.get(i).getDurataEpisodio().toString() +"minuti");
            durataLabel.getStyleClass().add("durata-episodio");
            episodioCard.getChildren().addAll(numeroLabel, durataLabel);
            contenitore.getChildren().add(episodioCard);
        }
    }

    private void aggiornaBottone(){
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
        this.presenteInLista=true;
        aggiornaBottone();
    }

    private void tornaIndietro(){
        ModelContainerView.getInstance().getViewFactory().tornaAllaPaginaPrecedente();
    }

    private void caricaPiattaforme(List<Piattaforma> piattaforme) {
        StringBuilder sb = new StringBuilder();
        for(Piattaforma p:piattaforme){
            sb.append(p.getNomePiattaforma()).append(" ");
        }
        this.labelPiattaforme.setText(sb.toString());
    }
}
