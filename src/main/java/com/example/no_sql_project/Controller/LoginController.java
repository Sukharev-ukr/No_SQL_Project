package com.example.no_sql_project;

import com.example.no_sql_project.DAO.EmployeeDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Hyperlink forgotLink;

    private EmployeeDAO employeeDAO = new EmployeeDAO(); // DAO for interacting with MongoDB

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();


        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or password cannot be empty.");
            return;
        }


        Document user = employeeDAO.findEmployeeByNameAndPassword(username, password);

        if (user != null) {
            loadDashboard();
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void loadDashboard() {
        try {
            URL fxmlLocation = HelloApplication.class.getResource("/com/example/no_sql_project/Dashboard/Dashboard.fxml");
            if (fxmlLocation == null) {
                throw new IllegalStateException("Dashboard.fxml file not found at the specified path!");
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene dashboardScene = new Scene(fxmlLoader.load());
            Stage currentStage = (Stage) usernameField.getScene().getWindow(); // Get current stage from any UI element
            currentStage.setScene(dashboardScene);
            currentStage.setTitle("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Dashboard. Please try again.");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
