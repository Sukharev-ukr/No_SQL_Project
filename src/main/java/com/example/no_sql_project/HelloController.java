package com.example.no_sql_project;

import com.example.no_sql_project.DAO.EmployeeDAO;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;

import java.security.SecureRandom;

public class HelloController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckbox;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLoginButton() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate fields are not empty before proceeding
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and Password cannot be empty");
            return;
        }

        boolean authenticated = employeeDAO.authenticate(username, password);

        if (authenticated) {
            System.out.println("Login successful");
            errorLabel.setText(""); // Clear any error messages

            if (rememberMeCheckbox.isSelected()) {
                // Generate session token and store it in the Sessions collection
                String sessionToken = generateSessionToken();
                storeSessionToken(username, sessionToken);
            }

            // Proceed to the next scene after successful login (if applicable)

        } else {
            // Provide user feedback for incorrect login credentials
            errorLabel.setText("Invalid username or password");
        }
    }

    private String generateSessionToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // Generating a 32-byte session token
        random.nextBytes(bytes);
        return bytesToHex(bytes);
    }

    private void storeSessionToken(String username, String token) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Document session = new Document("username", username)
                .append("token", token)
                .append("expiresAt", System.currentTimeMillis() + 2_592_000_000L); // One month in milliseconds

        employeeDAO.createEntry("Sessions", session);
    }

    // Utility method to convert byte array to hexadecimal String
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
