package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.UI.View.BannerTitolo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
    public ListView<TitoloData> listConsigliati;
    public ListView <TitoloData>listPopolari;
    public ListView<TitoloData> listGeneri;
    public record TitoloData(String nome, int voto, String imageUrl) {}
    public ListView<TitoloData> listNovita;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listNovita.setFixedCellSize(160);
        listNovita.setPrefHeight(280);
        listConsigliati.setPrefHeight(280);
        listPopolari.setPrefHeight(280);
        listGeneri.setPrefHeight(280);
        popolaNovita();
        popolaPopolari();
        popolaGeneri();
        popolaConsigliati();


    }
    public void popolaNovita(){
        listNovita.setCellFactory(lv ->new ListCell<>(){
            private final BannerTitolo bannerTitolo = new BannerTitolo();
            @Override
            protected void updateItem(TitoloData data, boolean empty) {
                super.updateItem(data, empty);
                if(empty || data == null){
                    setGraphic(null);
                }else {
                    bannerTitolo.update(data.nome, data.voto,"/it/unical/serialmente/UI/Images/IconeAlert/information.png");
                    setGraphic(bannerTitolo);
                }
            }
        });

        ObservableList<TitoloData> dati = FXCollections.observableArrayList();
        for (int i = 0; i < 200; i++) {
            dati.add(new TitoloData("Titolo " + i, (i % 10) + 1, null /*url*/));
        }
        listNovita.setItems(dati);
    }

    public void popolaPopolari(){
        listPopolari.setCellFactory(lv ->new ListCell<>(){
            private final BannerTitolo bannerTitolo = new BannerTitolo();
            @Override
            protected void updateItem(TitoloData data, boolean empty) {
                super.updateItem(data, empty);
                if(empty || data == null){
                    setGraphic(null);
                }else {
                    bannerTitolo.update(data.nome, data.voto,"/it/unical/serialmente/UI/Images/IconeAlert/information.png");
                    setGraphic(bannerTitolo);
                }
            }
        });

        ObservableList<TitoloData> dati = FXCollections.observableArrayList();
        for (int i = 0; i < 200; i++) {
            dati.add(new TitoloData("Titolo " + i, (i % 10) + 1,null));
        }
        listPopolari.setItems(dati);
    }
    public void popolaConsigliati(){
        listConsigliati.setCellFactory(lv ->new ListCell<>(){
            private final BannerTitolo bannerTitolo = new BannerTitolo();
            @Override
            protected void updateItem(TitoloData data, boolean empty) {
                super.updateItem(data, empty);
                if(empty || data == null){
                    setGraphic(null);
                }else {
                    bannerTitolo.update(data.nome, data.voto,"/it/unical/serialmente/UI/Images/IconeAlert/information.png");
                    setGraphic(bannerTitolo);
                }
            }
        });

        ObservableList<TitoloData> dati = FXCollections.observableArrayList();
        for (int i = 0; i < 200; i++) {
            dati.add(new TitoloData("Titolo " + i, (i % 10) + 1, null));
        }
        listConsigliati.setItems(dati);
    }

    public void popolaGeneri(){
        listGeneri.setCellFactory(lv ->new ListCell<>(){
            private final BannerTitolo bannerTitolo = new BannerTitolo();
            @Override
            protected void updateItem(TitoloData data, boolean empty) {
                super.updateItem(data, empty);
                if(empty || data == null){
                    setGraphic(null);
                }else {
                    bannerTitolo.update(data.nome, data.voto,"/it/unical/serialmente/UI/Images/IconeAlert/information.png");
                    setGraphic(bannerTitolo);
                }
            }
        });

        ObservableList<TitoloData> dati = FXCollections.observableArrayList();
        for (int i = 0; i < 200; i++) {
            dati.add(new TitoloData("Titolo " + i, (i % 10) + 1, null ));
        }
        listGeneri.setItems(dati);
    }
}
