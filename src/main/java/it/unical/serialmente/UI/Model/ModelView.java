package it.unical.serialmente.UI.Model;

import it.unical.serialmente.UI.Controller.RegistrazioneController;
import it.unical.serialmente.UI.View.ViewFactory;

public class ModelView {
    // unica istanza di Model (singleton)
    private RegistrazioneController registrazioneController;
    private static ModelView model;
    private final ViewFactory viewFactory;


    // il costruttore di Model DEVE essere privato (singleton)
    private ModelView() {
        this.viewFactory = new ViewFactory();
    }

    // GETTER
    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    public RegistrazioneController getRegistrazioneController() {return registrazioneController;}

    // SETTER
    public void setRegistrazioneController(RegistrazioneController registrazioneController) { this.registrazioneController = registrazioneController; }


    // ottengo l'instanza di Model (singleton)
    // synchronized per evitare che più thread possano accedere a Model contemporaneamente
    public static synchronized ModelView getInstance() {
        if (model == null) {
            model = new ModelView();
        }
        return model;
    }

    public static synchronized void invalidate() {
        model = null;
    }
}
