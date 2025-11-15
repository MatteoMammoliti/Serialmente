package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelSezioneUtente;
import it.unical.serialmente.UI.View.PosterSezioneUtente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerSezioneUtente implements Initializable {
    public Label mesiTempoSerie;
    public Label giorniTempoSerie;
    public Label oreTempoSerie;
    public Label numeroEpisodiVisti;
    public Label mesiTempoFim;
    public Label giorniTempoFilm;
    public Label oreTempoFilm;
    public Label numeroFilmVisti;

    public record TitoloDato(Titolo titolo) {};
    public VBox contenitoreSalutiUtente;
    public Label labelSalutiUtente;
    public VBox contenitoreStatistiche;
    public ScrollPane scrollPaneStatistiche;
    public HBox contenitoreCardStatistiche;
    public VBox contenitoreSerieTvVisionate;
    public Button btnMostraSerieVisionate;
    public ListView<TitoloDato> listSerieVisionate;
    public VBox contenitoreSerieTvPreferite;
    public Button btnMostraSeriePreferite;
    public ListView<TitoloDato> listSeriePreferite;
    public VBox contenitoreFilmVisionati;
    public Button btnMostraFilmVisionati;
    public ListView<TitoloDato> listFilmVisionati;
    public VBox contenitoreFimPreferiti;
    public Button btnMostraFilmPreferiti;
    public ListView<TitoloDato> listFilmPreferiti;
    private final ModelSezioneUtente modelSezioneUtente = new ModelSezioneUtente();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.contenitoreSalutiUtente.setPrefHeight(300);
        int dimensioneBannerini = 250;
        this.listSeriePreferite.setPrefHeight(dimensioneBannerini);
        this.listSerieVisionate.setPrefHeight(dimensioneBannerini);
        this.listFilmPreferiti.setPrefHeight(dimensioneBannerini);
        this.listFilmVisionati.setPrefHeight(dimensioneBannerini);
        this.contenitoreCardStatistiche.setPadding(new Insets(15));
        try {
            aggiornaListe();
            caricaStatistiche();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void aggiornaListe() throws SQLException {
        caricaSezione(listSerieVisionate,"Visionati","SerieTv");
        caricaSezione(listSeriePreferite,"Preferiti","SerieTv");
        caricaSezione(listFilmVisionati,"Visionati","Film");
        caricaSezione(listFilmPreferiti,"Preferiti","Film");
    }

    public void caricaSezione(ListView<TitoloDato> lista, String tipoLista,String tipoTitolo) throws SQLException {
        List<Titolo> titoli = modelSezioneUtente.getTitoliInLista(tipoLista,tipoTitolo);
        boolean preferito;
        if(tipoLista.equals("Preferiti")) {preferito = true;} else {
            preferito = false;
        }
        lista.setCellFactory(lv ->new ListCell<>(){
            private final PosterSezioneUtente bannerTitolo = new PosterSezioneUtente(preferito);
            @Override
            protected void updateItem(TitoloDato data, boolean empty) {
                super.updateItem(data, empty);
                if(empty || data == null){
                    setGraphic(null);
                }else {
                    bannerTitolo.update(data.titolo);
                    bannerTitolo.clickPreferito(()->{
                        try {
                            modelSezioneUtente.rendiTitoloPreferito(data.titolo);
                            aggiornaListe();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    bannerTitolo.clickElimina(()->{
                        try {
                            modelSezioneUtente.togliTitoloDaiPreferiti(data.titolo);
                            aggiornaListe();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    setGraphic(bannerTitolo);
                }
            }
        });

        ObservableList<TitoloDato> dati = FXCollections.observableArrayList();
        for (Titolo titolo : titoli) {
            dati.add(new TitoloDato(titolo));
        }
        lista.setItems(dati);
    }

    public void caricaStatistiche() throws Exception {
        this.numeroFilmVisti.setText(modelSezioneUtente.getNmeroFilmVisionati().toString());

        List<Integer> oreGiornoMesiFilm= modelSezioneUtente.getOreGiorniMesiVisionatiFilm();
        this.mesiTempoFim.setText(oreGiornoMesiFilm.get(0).toString());
        this.giorniTempoFilm.setText(oreGiornoMesiFilm.get(1).toString());
        this.oreTempoFilm.setText(oreGiornoMesiFilm.get(2).toString());

        ModelSezioneUtente.StatisticheSerieTv statisticheSerieTv = modelSezioneUtente.getStatisticheSerieTv();
        this.mesiTempoSerie.setText(statisticheSerieTv.durate().get(0).toString());
        this.giorniTempoSerie.setText(statisticheSerieTv.durate().get(1).toString());
        this.oreTempoSerie.setText(statisticheSerieTv.durate().get(2).toString());
        this.numeroEpisodiVisti.setText(statisticheSerieTv.episodiVisti().toString());
    }
}