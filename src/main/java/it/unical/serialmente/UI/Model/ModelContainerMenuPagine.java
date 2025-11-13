package it.unical.serialmente.UI.Model;

import it.unical.serialmente.UI.Controller.Autenticazione.LoginController;
import it.unical.serialmente.UI.Controller.ContainerMenuPagineController;
import it.unical.serialmente.UI.View.ViewFactory;

public class ModelContainerMenuPagine {

    private ContainerMenuPagineController  menuPagineController;

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
    public ViewFactory getViewFactory() { return viewFactory; }
    public ContainerMenuPagineController getMenuPagineController() { return this.menuPagineController; }

    // SETTER
    public void setMenuPagineController(ContainerMenuPagineController controller) { this.menuPagineController = controller; }
}
