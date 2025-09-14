module com.example.fuckaround {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires java.desktop;

    opens com.example.fuckaround to javafx.fxml;
    exports com.example.fuckaround;
}