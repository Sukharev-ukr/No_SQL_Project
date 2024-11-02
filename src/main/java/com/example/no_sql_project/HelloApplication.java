package com.example.no_sql_project;

import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.DAO.TicketDAO;
import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Model.Ticket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.management.relation.Role;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/no_sql_project/Login/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);



        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
        //BaseDAO.testFunction();
    }
}