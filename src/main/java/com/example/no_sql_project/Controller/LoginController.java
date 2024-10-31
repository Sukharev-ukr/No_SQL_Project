package com.example.no_sql_project.Controller;

import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private EmployeeDAO employeeDAO = new EmployeeDAO(); // DAO for interacting with MongoDB

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or password cannot be empty.");
            return;
        }

        // Check credentials in MongoDB
        Document user = employeeDAO.findEmployeeByNameAndPassword(username, password);

        if (user != null) {
            loadDashboard(); // Proceed to dashboard if user is found
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void loadDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/no_sql_project/Dashboard/Dashboard.fxml"));
            fxmlLoader.setController(new DashboardController());
            Scene dashboardScene = new Scene(fxmlLoader.load());
            Stage currentStage = (Stage) usernameField.getScene().getWindow(); // Get current stage from any UI element
            currentStage.setScene(dashboardScene);
            currentStage.setTitle("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Dashboard. Please try again.");
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
