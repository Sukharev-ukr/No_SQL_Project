package com.example.no_sql_project;

import com.example.no_sql_project.DAO.BaseDAO;
import com.example.no_sql_project.DAO.EmployeeDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        //BaseDAO mongoDB = new BaseDAO();
        EmployeeDAO test = new EmployeeDAO();
        System.out.println(test.findEmplyeeByID(new ObjectId("66fa672e0b885326bc77c6a9")));

    }

    public static void main(String[] args)
    {
        launch();
        //BaseDAO.testFunction();
    }
}