package it.unical.serialmente.UI.Model;

import it.unical.serialmente.UI.Controller.Autenticazione.LoginController;
import it.unical.serialmente.UI.Controller.Autenticazione.RegistrazioneController;
import it.unical.serialmente.UI.View.ViewFactory;

public class ModelContainerMenuPagine {

    /**
     * Istanze singleton dei controller
     */
    private RegistrazioneController registrazioneController;
    private LoginController loginController;

    private static ModelContainerMenuPagine model;

    private final ViewFactory viewFactory;


    private ModelContainerMenuPagine() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized ModelContainerMenuPagine getInstance() {
        if (model == null) {
            model = new ModelContainerMenuPagine();
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
