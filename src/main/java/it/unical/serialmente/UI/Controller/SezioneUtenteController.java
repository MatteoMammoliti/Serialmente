package it.unical.serialmente.UI.Controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SezioneUtenteController implements Initializable {
    public record DatoTitolo(String nome,double voto,String imgUrl){};
    public VBox contenitoreSalutiUtente;
    public Label labelSalutiUtente;
    public VBox contenitoreStatistiche;
    public ScrollPane scrollPaneStatistiche;
    public HBox contenitoreCardStatistiche;
    public VBox contenitoreSerieTvVisionate;
    public Button btnMostraSerieVisionate;
    public ListView<DatoTitolo> listSerieVisionate;
    public VBox contenitoreSerieTvPreferite;
    public Button btnMostraSeriePreferite;
    public ListView<DatoTitolo> listSeriePreferite;
    public VBox contenitoreFilmVisionati;
    public Button btnMostraFilmVisionati;
    public ListView<DatoTitolo> listFilmVisionati;
    public VBox contenitoreFimPreferiti;
    public Button btnMostraFilmPreferiti;
    public ListView<DatoTitolo> listFilmPreferiti;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
