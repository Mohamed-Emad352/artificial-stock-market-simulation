module com.example.artificialstockmarketsimulation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens GUI to javafx.fxml;
    exports GUI;
    exports Core;
}