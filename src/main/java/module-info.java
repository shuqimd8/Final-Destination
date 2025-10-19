module com.learneria.controller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens com.learneria to javafx.fxml;
    exports com.learneria;
}