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
    requires javafx.graphics;
    requires java.desktop;
    requires jakarta.mail;

    exports it.unical.serialmente;
    opens it.unical.serialmente.UI.Controller to javafx.fxml;
    opens it.unical.serialmente.UI.Controller.Autenticazione to javafx.fxml;
    opens it.unical.serialmente.UI.Controller.Autenticazione.CambioPW to javafx.fxml;
    opens it.unical.serialmente.UI.Controller.PagineNavigazione to javafx.fxml;
}