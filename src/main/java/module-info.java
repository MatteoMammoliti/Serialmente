module it.unical.serialmente.UI {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires java.sql;
    requires spring.security.crypto;
    requires javafx.base;

    opens it.unical.serialmente.UI to javafx.fxml;
    opens it.unical.serialmente.UI.Controller to javafx.fxml;
    exports it.unical.serialmente.UI;
}