package it.unical.serialmente.UI.Model;

import it.unical.serialmente.UI.Controller.ContainerViewController;
import it.unical.serialmente.UI.View.ViewFactory;

public class ModelContainerView {

    private ContainerViewController menuPagineController;

    private static ModelContainerView model;

    private final ViewFactory viewFactory;


    private ModelContainerView() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized ModelContainerView getInstance() {
        if (model == null) {
            model = new ModelContainerView();
        }
        return model;
    }


    // GETTER
    public ViewFactory getViewFactory() { return viewFactory; }
    public ContainerViewController getMenuPagineController() { return this.menuPagineController; }

    // SETTER
    public void setMenuPagineController(ContainerViewController controller) { this.menuPagineController = controller; }
}
