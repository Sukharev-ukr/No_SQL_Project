module com.example.no_sql_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.no_sql_project to javafx.fxml;
    exports com.example.no_sql_project;
}