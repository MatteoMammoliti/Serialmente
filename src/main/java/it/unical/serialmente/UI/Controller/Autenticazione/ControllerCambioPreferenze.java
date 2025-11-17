package it.unical.serialmente.UI.Controller.Autenticazione;

import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import it.unical.serialmente.UI.Model.ModelAutenticazione.ModelCambioPreferenze;
import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
        List<Genere> tuttiGeneri= modelCambioPreferenze.getTuttiGeneri();
        modelCambioPreferenze.riempiGeneriPrecedenti(generiUtente);
        for(Genere g:tuttiGeneri){
            CheckMenuItem cb = new CheckMenuItem(g.getNomeGenere());
            if (generiUtente.contains(g)){
                cb.setSelected(true);
                elencoGeneri.getItems().add(cb);
            }else{
                cb.setSelected(false);
                elencoGeneri.getItems().add(cb);
            }
            cb.setOnAction(e -> {
                if(cb.isSelected()){
                    modelCambioPreferenze.addGenere(g.getIdGenere());
                }
                else modelCambioPreferenze.rimuoviGenere(g.getIdGenere());
                elencoGeneri.show();
                if(!cambiamenti){
                    cambiamenti=true;
                }
            });
        }
    }
    private void caricaPiattaforme(){
        List<Piattaforma> tuttePiattaforme= modelCambioPreferenze.getTuttePiattafome();
        modelCambioPreferenze.riempiPiattformePrecedenti(piattaformeUtente);
        for(Piattaforma g:tuttePiattaforme){
            CheckMenuItem cb = new CheckMenuItem(g.getNomePiattaforma());
            cb.setStyle("fx-text-fill: white;");
            if (piattaformeUtente.contains(g)){
                cb.setSelected(true);
                elencoPiattaforme.getItems().add(cb);
            }else{
                cb.setSelected(false);
                elencoPiattaforme.getItems().add(cb);
            }
            cb.setOnAction(e -> {
                if(cb.isSelected()){
                    modelCambioPreferenze.addPiattaforma(g.getIdPiattaforma());
                }
                else modelCambioPreferenze.rimuoviPiattaforma(g.getIdPiattaforma());
                elencoPiattaforme.show();
                if(!cambiamenti){
                    cambiamenti=true;
                }
            });
        }
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
