module com.example.no_sql_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;

    opens com.example.no_sql_project to javafx.fxml;
    opens com.example.no_sql_project.DAO to javafx.fxml;
    exports com.example.no_sql_project;
    exports com.example.no_sql_project.DAO;
}