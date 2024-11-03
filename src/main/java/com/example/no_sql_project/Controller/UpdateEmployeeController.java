package com.example.no_sql_project.Controller;

import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Service.EmployeeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class UpdateEmployeeController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> userTypeComboBox;

    @FXML
    private TextField passwordField;

    EmployeeService employeeService = new EmployeeService();

    private Employee loggedInEmployee;

    public UpdateEmployeeController(Employee loggedInEmployee){
        this.loggedInEmployee = loggedInEmployee;
    }

    @FXML
    public void initialize() {

        String[] name = loggedInEmployee.getName().split(" ");
        String firstName = name[0];
        String lastName = name[1];

        firstNameField.setText(firstName);
        lastNameField.setText(lastName);

        userTypeComboBox.setValue(loggedInEmployee.getRole());

        ObservableList<String> options = FXCollections.observableArrayList("ServiceDesk", "Employee");
        userTypeComboBox.setItems(options);

        passwordField.setText(loggedInEmployee.getPassword());
    }

    @FXML
    public void updateUser() {
        if (validateInput()) {
            String firstName = firstNameField.getText();
            String password = passwordField.getText();
            String lastName = lastNameField.getText();
            String userType = userTypeComboBox.getValue();
            String privileges = "basic";

            String fullName = firstName + " " + lastName;

            if ("ServiceDesk".equals(userType)) {
                privileges = "admin";
            }

            Employee employee = new Employee(loggedInEmployee.getId(),fullName,password,userType,privileges);

            employeeService.passAddEmployee(employee);

            close();
        } else {
            System.out.println("User not created");
        }
    }

    private boolean validateInput() {
        boolean inputValid = true;
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || userTypeComboBox.getValue() == null || passwordField.getText().isEmpty() || firstNameField.getLength() < 3 || lastNameField.getLength() < 3 )
        {
            inputValid = false;
        }
        return inputValid;
    }

    @FXML
    private void cancel(){
        close();
    }

    private void close() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

}
