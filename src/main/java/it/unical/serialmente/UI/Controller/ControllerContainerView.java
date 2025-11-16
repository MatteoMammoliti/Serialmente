package it.unical.serialmente.UI.Controller;

import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.TechnicalServices.Utility.ThreadPool;
import it.unical.serialmente.UI.Model.ModelContainerView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerContainerView implements Initializable {

    @FXML private BorderPane contenitoreView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ModelContainerView.getInstance().setMenuPagineController(this);
        ModelContainerView.getInstance().getViewFactory().getFinestraAttuale().addListener((observable, oldValue, newValue) -> {

            Node nuovaView = switch (newValue) {
                case "Film" -> ModelContainerView.getInstance().getViewFactory().getPaginaFilm();
                case "SerieTV" -> ModelContainerView.getInstance().getViewFactory().getPaginaSerieTV();
                case "Watchlist" -> ModelContainerView.getInstance().getViewFactory().getWatchlist();
                case "ProfiloUtente" -> {
                    try {
                        yield ModelContainerView.getInstance().getViewFactory().getPaginaProfiloUtente();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case "Logout" -> {
                    SessioneCorrente.resetSessioneCorrente();
                    ModelContainerView.getInstance().getViewFactory().closeStage(
                            (Stage) contenitoreView.getScene().getWindow()
                    );
                    ModelContainerView.getInstance().getViewFactory().mostraFinestraLogin();
                    ThreadPool.shutdown();
                    ModelContainerView.getInstance().getViewFactory().getFinestraAttuale().set("");
                    yield null;
                }
                default -> null;
            };

            if (nuovaView != null) {
                contenitoreView.setCenter(nuovaView);
            }
        });
    }

    public BorderPane getContenitoreView() {
        return contenitoreView;
    }
}