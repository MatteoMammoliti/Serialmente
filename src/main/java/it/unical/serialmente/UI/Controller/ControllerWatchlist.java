package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.Film;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelWatchlist;
import it.unical.serialmente.UI.View.BannerWatchlistFilm;
import it.unical.serialmente.UI.View.BannerWatchlistSerieTv;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

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
                if ("FilmS".equalsIgnoreCase(tipo)) {
                    Film titolo = (Film)t;
                    bannerFilm.update(titolo.getNomeTitolo(),titolo.getDurataMinuti(),titolo.getImmagine());
                    setGraphic(bannerFilm);
                } else if ("SerieTv".equalsIgnoreCase(tipo)) {
                    bannerSerie.update(t.getNomeTitolo(),model.getNumeroStagione(t.getIdTitolo()),model.getNumeroEpisodio(t.getIdTitolo()),
                            model.getNomeEpisodio(t.getIdTitolo()),t.getImmagine());
                    setGraphic(bannerSerie);
                } else {
                    setGraphic(null);
                }
            }
        });

        listTitoli.setItems(FXCollections.observableArrayList(titoli));
    }
}
