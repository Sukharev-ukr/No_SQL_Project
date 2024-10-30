package com.example.no_sql_project.Controller;

import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.DAO.TicketDAO;
import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Model.Ticket;
import com.example.no_sql_project.Service.TicketService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class OverviewTicket {
    @FXML
    private Button createIncidentButton;
    @FXML
    private TableView ticketsTable;
    @FXML
    private TableColumn<Ticket, String> typeColumn;
    @FXML
    private TableColumn<Ticket, String> userColumn;
    @FXML
    private TableColumn<Ticket, String> dateColumn;
    @FXML
    private TableColumn<Ticket, String> statusColumn;
    @FXML
    private TableColumn<Ticket, String> descriptionColumn;
    @FXML
    private TableColumn<Ticket, String> priorityColumn;

    private TicketService ticketService = new TicketService();
    private Employee loggedInEmployee;

    public void setLoggedInEmployee(Employee loggedInEmployee) { // use it in when loadDashpoard
        this.loggedInEmployee = loggedInEmployee;
    }

    private void loadTicketBaseOnRole() {
        ArrayList<Ticket> tickets;
        if (loggedInEmployee.getRole().equals("ServiceDesk"))
        {
            tickets = ticketService.getAllTickets();
        }
        else {
            tickets = ticketService.getEmployeeTickets(loggedInEmployee.getId().toString());
        }
        ticketsTable.getItems().clear();
        ticketsTable.getItems().addAll(tickets);
    }

    @FXML
    public void initialize() {
        // Initialize table columns (ensure Ticket properties match these)
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("ticketDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
    }

    public void switchToCreateIncident() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("com.example.no_sql_project.Controller.Createincidentticket"));
            Parent root = fxmlLoader.load();

            // Get the controller of CreateIncident and pass logged-in employee data if needed
            Createincidentticket createIncidentController = fxmlLoader.getController();
            createIncidentController.setLoggedInUsername(loggedInEmployee); // Pass employee data

            // Switch to the CreateIncident scene
            Scene createIncidentScene = new Scene(root);
            Stage currentStage = (Stage) createIncidentButton.getScene().getWindow();
            currentStage.setScene(createIncidentScene);
            currentStage.setTitle("Create Incident");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Create Incident screen.");
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
