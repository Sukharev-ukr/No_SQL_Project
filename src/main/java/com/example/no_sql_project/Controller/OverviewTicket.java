package com.example.no_sql_project.Controller;

import com.example.no_sql_project.Controller.Createincidentticket;
import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Model.Priority;
import com.example.no_sql_project.Model.Status;
import com.example.no_sql_project.Model.Ticket;
import com.example.no_sql_project.Service.TicketService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OverviewTicket {
    @FXML
    private ChoiceBox<String> prioritySortChoiceBox;
    @FXML
    private Button createIncidentButton;
    @FXML
    private TextField filterTextField;
    @FXML
    private Button deleteButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button escalationButton;
    @FXML
    private TableView<Ticket> ticketTable;
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
    private Button manageUsersButton;
    private TicketService ticketService = new TicketService();
    private Employee loggedInEmployee;
    private ObservableList<Ticket> allTickets;

    public void setLoggedInEmployee(Employee loggedInEmployee) {
        this.loggedInEmployee = loggedInEmployee;
        initializeDashboardBasedOnRole();
    }

    private void initializeDashboardBasedOnRole() {
        if (loggedInEmployee.getRole().equalsIgnoreCase("ServiceDesk")) {
            // ServiceDesk gets full privileges, including the ability to manage users
            manageUsersButton.setVisible(true);
            createIncidentButton.setVisible(true);
        } else {
            // Regular employee has limited functionality
            manageUsersButton.setVisible(false); // Hide user management button for regular employees
            createIncidentButton.setVisible(true);
        }
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Display employee name instead of employee ID
        userColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmployeeName())
        );

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTicketDate() != null ?
                        cellData.getValue().getTicketDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "")
        );

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        // Initialize the sort dropdown to trigger the sort event
        if (prioritySortChoiceBox != null) {
            prioritySortChoiceBox.getItems().addAll("High to Low", "Low to High");
            prioritySortChoiceBox.setOnAction(event -> handleSortTickets());
        }
        if (loggedInEmployee != null && loggedInEmployee.getRole().equals("ServiceDesk")) {
            deleteButton.setDisable(true);
            escalationButton.setDisable(true);
            closeButton.setDisable(true);
        }

        loadTicketBaseOnRole();
        loadAllTickets();
        setupFilter();
    }

     @FXML
    private void handleSortTickets() {
        String selectedSortOrder = prioritySortChoiceBox.getValue();
        ArrayList<Ticket> tickets;
        if (selectedSortOrder.equals("High to Low")) {
            tickets = ticketService.getTicketsSortedByPriorityDescending();
        } else if (selectedSortOrder.equals("Low to High")) {
            tickets = ticketService.getTicketsSortedByPriorityAscending();
        } else {
            tickets = ticketService.getTicketsWithEmployeeNames(); // Default unsorted
        }
        ticketTable.getItems().clear();
        ticketTable.getItems().addAll(tickets);
    }


    private void loadTicketBaseOnRole() {
        ArrayList<Ticket> tickets;
        if (loggedInEmployee.getRole().equals("ServiceDesk")) {
            tickets = ticketService.getTicketsWithEmployeeNames();
        } else {
            tickets = ticketService.getEmployeeTickets(loggedInEmployee.getId().toString());
        }
        ticketTable.getItems().clear();
        ticketTable.getItems().addAll(tickets);
    }

    //////////////////////////////////////// close ticket and escalate ticket
    @FXML
    private void handleEscalateTicket() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            // Check the current priority and escalate if possible
            Priority currentPriority = selectedTicket.getPriority();
            if (currentPriority == Priority.low) {
                selectedTicket.setPriority(Priority.normal);
            } else if (currentPriority == Priority.normal) {
                selectedTicket.setPriority(Priority.high);
            } else if (currentPriority == Priority.high) {
                showAlert("Escalate Ticket", "The ticket is already at the highest priority level.");
                return;  // Exit if no further escalation is possible
            }

            // Update the ticket in the database
            ticketService.updateTicket(selectedTicket.getId(), selectedTicket);
            loadTicketBaseOnRole();  // Refresh the table to show the updated priority
            showAlert("Success", "The ticket priority has been escalated.");
        } else {
            showAlert("Error", "Please select a ticket to escalate.");
        }
    }
    @FXML
    private void handleCloseTicket() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            selectedTicket.setStatus(Status.closed);  // Update status to closed
            ticketService.updateTicket(selectedTicket.getId(), selectedTicket);
            loadTicketBaseOnRole();  // Refresh table
            showAlert("Success", "The ticket has been closed successfully.");
        } else {
            showAlert("Error", "Please select a ticket to close.");
        }
    }
    public void loadTicketsWithEmployeeNames() {
        ArrayList<Ticket> tickets = ticketService.getTicketsWithEmployeeNames();
        ticketTable.getItems().setAll(tickets);  // Refresh with new data
    }

    ////////////////////////////////////////////////////////// Switch To Other Screen
    @FXML
    public void switchToCreateIncident () {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/Tickets/Createincidentticket.fxml"));
            Parent root = fxmlLoader.load();

            // Get the controller of CreateIncident and pass logged-in employee data if needed
            Createincidentticket createIncidentController = fxmlLoader.getController();
            createIncidentController.setLoggedInUsername(loggedInEmployee);

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
    public void switchToUpdateTicket() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();

        if (selectedTicket != null) {
            try {
                // Load the UpdateTicket FXML file
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/Tickets/updateticket.fxml"));
                Parent root = fxmlLoader.load();

                // Get the UpdateTicketController and set the current ticket
                Updateticket updateTicketController = fxmlLoader.getController();
                updateTicketController.setCurrentTicket(selectedTicket);  // Pass the selected ticket to the UpdateTicketController
                updateTicketController.setLoggedInUsername(loggedInEmployee);

                // Create a new stage for the update ticket screen
                Stage stage = new Stage();
                stage.setTitle("Update Ticket");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Unable to load the Update Ticket screen.");
            }
        } else {
            // Show an alert if no ticket is selected
            showAlert("No Selection", "Please select a ticket to update.");
        }
    }

    ////////////////////////////////////////////////////////////////// Filter Ticket By UserName
    private void loadAllTickets() {
        allTickets = FXCollections.observableArrayList(ticketService.getTicketsWithEmployeeNames());
        ticketTable.setItems(allTickets);
    }

    private void setupFilter() {
        filterTextField.setOnKeyReleased(event -> filterTickets());
    }

    private void filterTickets() {
        String filterText = filterTextField.getText().toLowerCase().trim();

        if (filterText.isEmpty()) {
            // If the filter is empty, display all tickets
            ticketTable.setItems(allTickets);
        } else {
            // Filter tickets based on the entered username
            ObservableList<Ticket> filteredTickets = FXCollections.observableArrayList();
            for (Ticket ticket : allTickets) {
                if (ticket.getEmployeeName() != null && ticket.getEmployeeName().toLowerCase().contains(filterText)) {
                    filteredTickets.add(ticket);
                }
            }
            ticketTable.setItems(filteredTickets);
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

