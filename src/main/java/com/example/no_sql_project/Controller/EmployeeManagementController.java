package com.example.no_sql_project.Controller;


import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.Model.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.time.format.DateTimeFormatter;

public class EmployeeManagementController {

    @FXML
    private TableView<Employee> userTable;
    @FXML
    private TableColumn<Employee, String> idColumn;
    @FXML
    private TableColumn<Employee, String> fullNameColumn;
    @FXML
    private TableColumn<Employee, String> roleColumn;
    @FXML
    private TableColumn<Employee, String> ticketsColumn;

    public void initialize() {
        configureTableColumns();
        loadUsers();
    }

    private void configureTableColumns() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        ticketsColumn.setCellValueFactory(new PropertyValueFactory<>("ticketsCount"));
    }

    private void loadUsers() {
        ObservableList<Employee> showingObservableList = FXCollections.observableArrayList();//missing get all exmployees
        userTable.setItems(showingObservableList);
    }

    @FXML
    public void addUser(){
        //go to add user page
    }



}
