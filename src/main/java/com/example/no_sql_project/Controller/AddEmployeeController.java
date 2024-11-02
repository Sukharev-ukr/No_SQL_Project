package com.example.no_sql_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddEmployeeController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> userTypeComboBox;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private CheckBox sendPasswordCheckbox;

    // Initialization method
    @FXML
    public void initialize() {
        // Initialize your UI component if necessary
    }

    @FXML
    private void addUser() {
        if (validateInput()) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String userType = userTypeComboBox.getValue();
            boolean sendPassword = sendPasswordCheckbox.isSelected();

            //debug
            System.out.println("User Added: " + firstName + " " + lastName + " - " + userType);

            clearFields();
        } else {
            //error message
        }
    }

    private boolean validateInput() {
        boolean inputValid = true;
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                userTypeComboBox.getValue() == null || emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || locationComboBox.getValue() == null) {
            inputValid = false;
        }
        return inputValid;
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        userTypeComboBox.getSelectionModel().clearSelection();
        locationComboBox.getSelectionModel().clearSelection();
        sendPasswordCheckbox.setSelected(false);
    }
}

