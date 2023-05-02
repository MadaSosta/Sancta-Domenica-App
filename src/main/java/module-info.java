module application.sanctadomenica {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens javaFX to javafx.fxml;
    exports javaFX;
}