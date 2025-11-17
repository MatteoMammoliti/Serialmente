package it.unical.serialmente.UI.Controller.PagineNavigazione;

import it.unical.serialmente.Domain.model.Film;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelContainerView;
import it.unical.serialmente.UI.Model.PagineNavigazione.ModelWatchlist;
import it.unical.serialmente.UI.View.BannerWatchlistFilm;
import it.unical.serialmente.UI.View.BannerWatchlistSerieTv;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerWatchlist implements Initializable {
    public ListView<Titolo> listTitoli;
    public Button btnSfigliaTitoli;
    private final ModelWatchlist model = new ModelWatchlist();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            popolaLista();
        } catch (Exception e) {
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
            e.printStackTrace();
        }
    }

    public void popolaLista() throws Exception {
        List<Titolo> titoli = model.getTitoliInWatchlist();

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
                            e.printStackTrace();
                        }
                    });
                    bannerFilm.setRimuovi(()->{
                        try {
                            model.rimuoviFilmWatchlist(titolo);
                            popolaLista();
                        } catch (Exception e) {
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
                            throw new RuntimeException(e);
                        }
                    });
                    bannerSerie.setVisionatoSerie(()->{
                        try {
                            model.rendiFilmVisionato(t);
                            popolaLista();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    bannerSerie.setSegnaEpisodio(()->{
                        try {
                            model.episodioSuccessivo(t);
                            popolaLista();
                        } catch (Exception e) {
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
            Parent paginaCorrente = listTitoli.getScene().getRoot();
            ModelContainerView.getInstance()
                    .getViewFactory()
                    .setPaginaPrecedente(paginaCorrente);

            FXMLLoader paginaRicerca = new FXMLLoader(getClass().getResource("/it/unical/serialmente/UI/Fxml/PagineNavigazione/sezioneRicerca.fxml"));
            Parent root = paginaRicerca.load();
            Stage stage = (Stage) listTitoli.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
