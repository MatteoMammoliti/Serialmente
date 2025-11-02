module it.unical.serialmente.UI {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens it.unical.serialmente.UI to javafx.fxml;
    exports it.unical.serialmente.UI;
}