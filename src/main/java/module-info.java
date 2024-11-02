module com.example.no_sql_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;
    requires java.management;

    // Export the root package to make it accessible to JavaFX for launching
    exports com.example.no_sql_project;

    // Export the controller package to make it accessible for general use
    exports com.example.no_sql_project.Controller;

    // Open the controller package for reflection access to JavaFX FXML
    opens com.example.no_sql_project.Controller to javafx.fxml;

    // If you need other classes (like DAO) to be accessed via FXML or JavaFX,
    // you may need to similarly open or export those packages.
}
