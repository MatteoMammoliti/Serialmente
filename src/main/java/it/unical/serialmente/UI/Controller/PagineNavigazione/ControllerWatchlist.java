package it.unical.serialmente.UI.Controller.PagineNavigazione;

import it.unical.serialmente.Domain.model.Film;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.TechnicalServices.Utility.AlertHelper;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.Model.PagineNavigazione.ModelWatchlist;
import it.unical.serialmente.UI.View.BannerWatchlistFilm;
import it.unical.serialmente.UI.View.BannerWatchlistSerieTv;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerWatchlist implements Initializable {
    private final ModelWatchlist model = new ModelWatchlist();

    @FXML
    private Label labelVuoto;
    @FXML private ListView<Titolo> listTitoli;
    @FXML private Button btnSfigliaTitoli;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            popolaLista();
        } catch (Exception e) {
            AlertHelper.nuovoAlert(
                    "Errore",
                    Alert.AlertType.ERROR,
                    "Qualcosa è andato storto!",
                    "Errore durante il recupero dei titoli in Watchlist. Riprovare!"
            );
            throw new RuntimeException(e);
        }

        this.btnSfigliaTitoli.setOnAction(e -> {
            apriRicerca();
        });
    }
    public void refresh() {
        try {
            popolaLista();
        } catch (Exception e) {
            AlertHelper.nuovoAlert(
                    "Errore",
                    Alert.AlertType.ERROR,
                    "Qualcosa è andato storto!",
                    "Errore durante il recupero dei titoli in Watchlist. Riprovare!"
            );
            throw new RuntimeException(e);
        }
    }

    public void popolaLista() throws Exception {
        List<Titolo> titoli = model.getTitoliInWatchlist();

        if(titoli.isEmpty()) {
            labelVuoto.setVisible(true);
            listTitoli.setItems(FXCollections.observableArrayList());
            return;
        }

        labelVuoto.setVisible(false);

        listTitoli.setCellFactory(lv -> new ListCell<>() {
            private final BannerWatchlistFilm bannerFilm = new BannerWatchlistFilm();
            private final BannerWatchlistSerieTv bannerSerie = new BannerWatchlistSerieTv();

            @Override
            protected void updateItem(Titolo t, boolean empty) {
                super.updateItem(t, empty);
                if (empty || t == null) {
                    setGraphic(null);
                    return;
                }

                String tipo = t.getTipologia();

                if ("Film".equalsIgnoreCase(tipo)) {

                    Film titolo = (Film)t;
                    bannerFilm.update(titolo.getNomeTitolo(),titolo.getDurataMinuti(),titolo.getImmagine());
                    bannerFilm.setVisionato(()->{
                        try {
                            model.rendiFilmVisionato(titolo);
                            popolaLista();
                        } catch (Exception e) {
                            AlertHelper.nuovoAlert(
                                    "Errore",
                                    Alert.AlertType.ERROR,
                                    "Qualcosa è andato storto!",
                                    "Errore durante l'aggiunta del Film nella lista Visionati. Riprovare!"
                            );
                            e.printStackTrace();
                        }
                    });

                    bannerFilm.setRimuovi(()->{
                        try {
                            model.rimuoviFilmWatchlist(titolo);
                            popolaLista();
                        } catch (Exception e) {
                            AlertHelper.nuovoAlert(
                                    "Errore",
                                    Alert.AlertType.ERROR,
                                    "Qualcosa è andato storto!",
                                    "Errore durante l'eliminazione del Film. Riprovare!"
                            );
                            throw new RuntimeException(e);
                        }
                    });
                    setGraphic(bannerFilm);

                } else if ("SerieTv".equalsIgnoreCase(tipo)) {

                    bannerSerie.update(t.getNomeTitolo(),model.getNumeroStagione(t.getIdTitolo()),model.getNumeroEpisodio(t.getIdTitolo()),
                            "",t.getImmagine());

                    bannerSerie.setRimuovi(()->{
                        try {
                            model.rimuoviSerieWatchlist(t);
                            popolaLista();
                        } catch (Exception e) {
                            AlertHelper.nuovoAlert(
                                    "Errore",
                                    Alert.AlertType.ERROR,
                                    "Qualcosa è andato storto!",
                                    "Errore durante l'eliminazione della Serie TV. Riprovare!"
                            );
                            throw new RuntimeException(e);
                        }
                    });

                    bannerSerie.setVisionatoSerie(()->{
                        try {
                            model.rendiFilmVisionato(t);
                            popolaLista();
                        } catch (Exception e) {
                            AlertHelper.nuovoAlert(
                                    "Errore",
                                    Alert.AlertType.ERROR,
                                    "Qualcosa è andato storto!",
                                    "Errore durante l'aggiunta della Serie TV nella lista Visionati. Riprovare!"
                            );
                            throw new RuntimeException(e);
                        }
                    });

                    bannerSerie.setSegnaEpisodio(()->{
                        try {
                            model.episodioSuccessivo(t);
                            popolaLista();
                        } catch (Exception e) {
                            AlertHelper.nuovoAlert(
                                    "Errore",
                                    Alert.AlertType.ERROR,
                                    "Qualcosa è andato storto!",
                                    "Errore durante il reperimento del prossimo episodip. Riprovare!"
                            );
                            throw new RuntimeException(e);
                        }
                    });
                    setGraphic(bannerSerie);

                } else {
                    setGraphic(null);
                }
            }
        });

        listTitoli.setItems(FXCollections.observableArrayList(titoli));
    }

    private void apriRicerca() {
        try {
            Parent parent = this.listTitoli.getScene().getRoot();
            ModelContainerView.getInstance()
                    .getViewFactory()
                    .setPaginaPrecedente(parent, this);

            FXMLLoader paginaRicerca = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/PagineNavigazione/sezioneRicerca.fxml"));
            Parent root = paginaRicerca.load();
            Stage stage = (Stage) listTitoli.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            AlertHelper.nuovoAlert(
                    "Errore",
                    Alert.AlertType.ERROR,
                    "Qualcosa è andato storto!",
                    "Errore durante l'apertura dell'area di ricerca'. Riprovare!"
            );
            throw new RuntimeException(e);
        }
    }
}