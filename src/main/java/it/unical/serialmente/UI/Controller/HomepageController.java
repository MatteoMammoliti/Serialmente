package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.Titolo;
import it.unical.serialmente.UI.Model.ModelHomepage;
import it.unical.serialmente.UI.View.BannerTitolo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
    private final ModelHomepage modelHomepage = new ModelHomepage();
    public ListView<TitoloData> listConsigliati;
    public ListView <TitoloData>listPopolari;
    public ListView<TitoloData> listGeneri;
    @FXML
    public ScrollPane scrollPrincipale;

    public record TitoloData(String nome, double voto, String imageUrl) {}
    public ListView<TitoloData> listNovita;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listNovita.setPrefHeight(250);
        listConsigliati.setPrefHeight(250);
        listPopolari.setPrefHeight(250);
        listGeneri.setPrefHeight(250);
        try {
            caricaSezione(this.listNovita,"Novita");
            caricaSezione(this.listGeneri,"Generi");
            caricaSezione(this.listConsigliati,"Consigliati");
            caricaSezione(this.listPopolari,"Popolari");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void caricaSezione(ListView<TitoloData> lista,String tipologia) throws Exception {
        List<Titolo> titoli = new ArrayList<>();
        switch (tipologia) {
            case "Novita":
                titoli = modelHomepage.getTitoliNovita();
                break;
            case "Popolari":
                titoli = modelHomepage.getTitoliPopolari();
                break;
            case "Consigliati":
                titoli = modelHomepage.getTitoliConsigliati();
                break;
            case "Generi":
                //Generi
            }
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
        for (int i = 0; i < titoli.size(); i++) {
            dati.add(new TitoloData(titoli.get(i).getNomeTitolo(), titoli.get(i).getVotoMedio(), titoli.get(i).getImmagine()));
        }
        lista.setItems(dati);

        }
    }
