package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelSezioneFilm;
import it.unical.serialmente.UI.View.BannerGeneri;
import it.unical.serialmente.UI.View.BannerTitolo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PaginaFilmController implements Initializable {
    private final ModelSezioneFilm modelSezioneFilm = new ModelSezioneFilm();
    public ListView<TitoloData> listConsigliati;
    public ListView <TitoloData>listPopolari;
    public ListView<String> listGeneri;
    @FXML
    public ScrollPane scrollPrincipale;

    public record TitoloData(String nome, double voto, String imageUrl) {}
    public ListView<TitoloData> listNovita;
    private final Integer dimensioneBannerini=250;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listNovita.setPrefHeight(dimensioneBannerini);
        listConsigliati.setPrefHeight(dimensioneBannerini);
        listPopolari.setPrefHeight(dimensioneBannerini);
        listGeneri.setPrefHeight(dimensioneBannerini);
        try {
            caricaSezioneGeneri(this.listGeneri);
            caricaSezione(this.listNovita,"Novita");
            caricaSezione(this.listConsigliati,"Consigliati");
            caricaSezione(this.listPopolari,"Popolari");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void caricaSezione(ListView<TitoloData> lista,String tipologia) throws Exception {
        List<Titolo> titoli = new ArrayList<>();

        titoli = switch (tipologia) {
            case "Novita" -> modelSezioneFilm.getTitoliNovita();
            case "Popolari" -> modelSezioneFilm.getTitoliPopolari();
            case "Consigliati" -> modelSezioneFilm.getTitoliConsigliati();
            default -> titoli;
        };

        lista.setCellFactory(lv ->new ListCell<>(){
            private final BannerTitolo bannerTitolo = new BannerTitolo();
            @Override
            protected void updateItem(TitoloData data, boolean empty) {
                super.updateItem(data, empty);
                if(empty || data == null){
                    setGraphic(null);
                }else {
                    bannerTitolo.update(data.nome, (int) data.voto,data.imageUrl);
                    setGraphic(bannerTitolo);
                }
            }
        });

        ObservableList<TitoloData> dati = FXCollections.observableArrayList();
        for (Titolo titolo : titoli) {
            dati.add(new TitoloData(titolo.getNomeTitolo(), titolo.getVotoMedio(), titolo.getImmagine()));
        }
        lista.setItems(dati);
    }

    public void caricaSezioneGeneri(ListView<String> lista) throws Exception {
        List<Genere> generi = modelSezioneFilm.getGeneri();

        lista.setCellFactory(lv -> new ListCell<>() {
            private final BannerGeneri bannerGenere = new BannerGeneri("movie");

            @Override
            protected void updateItem(String nome, boolean empty) {
                super.updateItem(nome, empty);
                if (empty || nome == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    bannerGenere.update(nome);
                    setGraphic(bannerGenere);
                    setText(null);
                }
            }
        });

        ObservableList<String> dati = FXCollections.observableArrayList();
        for (Genere genere : generi) {
            dati.add(genere.getNomeGenere());
        }
        lista.setItems(dati);
    }
}