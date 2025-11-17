package it.unical.serialmente.UI.Controller.Autenticazione;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelCambioPreferenze;
import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCambioPreferenze implements Initializable {
    public MenuButton elencoGeneri;
    public MenuButton elencoPiattaforme;
    public Label labelErrore;
    public Button btnProcedi;
    private final ModelCambioPreferenze modelCambioPreferenze = new  ModelCambioPreferenze();
    public Button btnTornaIndietro;
    boolean cambiamenti= false;
    List<Genere> generiUtente= modelCambioPreferenze.getGeneriPreferiti();
    List<Piattaforma> piattaformeUtente= modelCambioPreferenze.getPiattaformePreferite();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        caricaGeneri();
        caricaPiattaforme();
        btnProcedi.setOnAction(e -> {
            clickSalva();
        });
        btnTornaIndietro.setOnAction(e -> {clickIndietro();});
    }

    private void caricaGeneri(){
        List<Genere> tuttiGeneri = modelCambioPreferenze.getTuttiGeneri();
        modelCambioPreferenze.riempiGeneriPrecedenti(generiUtente);

        elencoGeneri.getItems().clear();

        VBox contenitore = new VBox(6);
        contenitore.setStyle("-fx-padding: 10;");

        for (Genere g : tuttiGeneri) {
            CheckBox cb = new CheckBox(g.getNomeGenere());
            cb.setSelected(generiUtente.contains(g));
            // cb.setStyle("-fx-text-fill: white;");

            cb.selectedProperty().addListener((obs, old, sel) -> {
                if (sel) modelCambioPreferenze.addGenere(g.getIdGenere());
                else     modelCambioPreferenze.rimuoviGenere(g.getIdGenere());

                elencoGeneri.show();
                cambiamenti = true;
            });

            contenitore.getChildren().add(cb);
        }

        ScrollPane scroll = new ScrollPane(contenitore);
        scroll.setPrefViewportHeight(200);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        CustomMenuItem custom = new CustomMenuItem(scroll);
        custom.setHideOnClick(false);

        elencoGeneri.getItems().add(custom);
    }

    private void caricaPiattaforme(){
        List<Piattaforma> tuttePiattaforme = modelCambioPreferenze.getTuttePiattafome();
        modelCambioPreferenze.riempiPiattformePrecedenti(piattaformeUtente);

        elencoPiattaforme.getItems().clear();

        VBox contenitore = new VBox(6);
        contenitore.setStyle("-fx-padding: 10;");

        for (Piattaforma p : tuttePiattaforme) {
            CheckBox cb = new CheckBox(p.getNomePiattaforma());
            cb.setSelected(piattaformeUtente.contains(p));
            //cb.setStyle("-fx-text-fill: white;");

            cb.selectedProperty().addListener((obs, old, sel) -> {
                if (sel) modelCambioPreferenze.addPiattaforma(p.getIdPiattaforma());
                else     modelCambioPreferenze.rimuoviPiattaforma(p.getIdPiattaforma());

                elencoPiattaforme.show();
                cambiamenti = true;
            });

            contenitore.getChildren().add(cb);
        }

        ScrollPane scroll = new ScrollPane(contenitore);
        scroll.setPrefViewportHeight(200);
        scroll.setFitToWidth(true);

        CustomMenuItem custom = new CustomMenuItem(scroll);
        custom.setHideOnClick(false);

        elencoPiattaforme.getItems().add(custom);
    }

    private void clickSalva(){
        if(!cambiamenti){
            return;
        }
        if(modelCambioPreferenze.idGeneri.isEmpty()){
            labelErrore.setVisible(true);
            return;
        }
        modelCambioPreferenze.applicaModifiche(generiUtente,piattaformeUtente);
        generiUtente=modelCambioPreferenze.getGeneriPreferiti();
        piattaformeUtente=modelCambioPreferenze.getPiattaformePreferite();
    }
    private void clickIndietro(){
        ModelContainerView.getInstance().getViewFactory().tornaAllaPaginaPrecedente();
    }
}
