package com.example.no_sql_project.Controller;
import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.Model.*;

import com.example.no_sql_project.Model.Type;
import com.example.no_sql_project.Service.TicketService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class Createincidentticket {
    @FXML
    private DatePicker datePickerReported;

    @FXML
    private ComboBox<Type> incidentTypeComboBox;

    @FXML
    private ComboBox<Priority> priorityComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button cancelButton;

    @FXML
    private Button submitTicketButton;
    private TicketService ticketService = new TicketService();
    private Map<String, ObjectId> userIdMap = new HashMap<>();
    private Employee loggedInUsername;

    public void setLoggedInUsername(Employee employee) {
        this.loggedInUsername = employee;
    }

    @FXML
    public void initialize() {
        incidentTypeComboBox.getItems().addAll(Type.values());
        priorityComboBox.getItems().addAll(Priority.values());
        loadEmployees();
    }

    private void loadEmployees() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        ArrayList<Employee> employees = employeeDAO.getAllEmployees();
         for (Employee employee : employees) {
             userIdMap.put(employee.getName(), employee.getId());
            // reportedByComboBox.getItems().add(employee.getName());
         }
    }

    private boolean validateInputs() {
        return datePickerReported.getValue() != null &&
                incidentTypeComboBox.getValue() != null &&
                //reportedUser.get != null &&
                priorityComboBox.getValue() != null &&
                !descriptionArea.getText().isEmpty();
    }

    public void handleSubmitTicket() {
        if (validateInputs()) {
            LocalDateTime ticketDate = datePickerReported.getValue().atStartOfDay();
            Priority priority = priorityComboBox.getValue();
            Type type = incidentTypeComboBox.getValue();
            String description = descriptionArea.getText();
            //String selectedUser = reportedByComboBox.getValue();
            //ObjectId selectedUserId = userIdMap.get(selectedUser);
            Ticket newTicket = new Ticket(loggedInUsername.getId().toHexString(), type, priority, Status.open, ticketDate, description);
            ticketService.addTickets(newTicket);
            showSuccessAlert("Ticket Created", "Ticker has been created successfully");
            //clearFormFields();
            closeWindow();
        }
        else {
            showAlert("Create Fail", "Please Fill In All Of Required Fields");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /*public void handleCancel() {
        clearFormFields();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/Tickets/OverviewTickets.fxml"));
            Parent root = fxmlLoader.load();

            OverviewTicket overviewTicketController = fxmlLoader.getController();
            //overviewTicketController.setLoggedInEmployee(); // Optional, if you need to pass user data

            Scene overviewScene = new Scene(root);
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.setScene(overviewScene);
            currentStage.setTitle("Overview Tickets");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Overview Ticket screen.");
        }
    }*/
    private void switchToTicketOverview() {
        try {
            // Load the TicketOverview scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/Tickets/OverviewTickets.fxml"));
            Parent root = fxmlLoader.load();

            // Get the controller for TicketOverview and call method to refresh table
            OverviewTicket overviewController = fxmlLoader.getController();
            overviewController.loadTicketsWithEmployeeNames();  // Refresh table in TicketOverview

            // Set the scene on the current stage
            Stage currentStage = (Stage) submitTicketButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Ticket Overview");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Ticket Overview screen.");
        }
    }

    private void clearFormFields() {
        datePickerReported.setValue(null);
        incidentTypeComboBox.setValue(null);
        //reportedByComboBox.setValue(null);
        priorityComboBox.setValue(null);
        descriptionArea.clear();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }
}
