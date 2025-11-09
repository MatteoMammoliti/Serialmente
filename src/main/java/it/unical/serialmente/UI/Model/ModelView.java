package it.unical.serialmente.UI.Model;

import it.unical.serialmente.UI.Controller.LoginController;
import it.unical.serialmente.UI.Controller.RegistrazioneController;
import it.unical.serialmente.UI.View.ViewFactory;

public class ModelView {

    /**
     * Istanze singleton dei controller
     */
    private RegistrazioneController registrazioneController;
    private LoginController loginController;

    private static ModelView model;

    private final ViewFactory viewFactory;


    private ModelView() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized ModelView getInstance() {
        if (model == null) {
            model = new ModelView();
        }
        return model;
    }


    // GETTER
    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    public RegistrazioneController getRegistrazioneController() {
        return registrazioneController;
    }
    public LoginController getLoginController() { return loginController; }

    // SETTER
    public void setRegistrazioneController(RegistrazioneController registrazioneController) {
        this.registrazioneController = registrazioneController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public static synchronized void invalidate() {
        model = null;
    }
}
