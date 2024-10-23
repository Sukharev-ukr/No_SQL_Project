module com.example.no_sql_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
    requires org.junit.jupiter.api;


    opens com.example.no_sql_project to javafx.fxml;
    exports com.example.no_sql_project;
}