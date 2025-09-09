module com.example.sql {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sql to javafx.fxml;
    exports com.example.sql;
}