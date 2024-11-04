package com.example.no_sql_project.Controller;

import com.example.no_sql_project.Controller.Createincidentticket;
import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Model.Priority;
import com.example.no_sql_project.Model.Status;
import com.example.no_sql_project.Model.Ticket;
import com.example.no_sql_project.DAO.EmployeeDAO;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private TicketService ticketService = new TicketService();
    private Employee loggedInEmployee;
    private ObservableList<Ticket> allTickets;

    //Fred Individual Functionality
    @FXML
    private Button transferButton;
    @FXML
    private ComboBox<Employee> employeesCB;

    EmployeeDAO employeeDAO = new EmployeeDAO();
    //

    public OverviewTicket(Employee loggedInEmployee){
        this.loggedInEmployee = loggedInEmployee;
    }


    @FXML
    public void initialize() {
        // Initialize table columns
        typeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType().toString())
        );

        // Display employee name instead of employee ID
        userColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmployeeName())
        );

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTicketDate() != null ?
                        cellData.getValue().getTicketDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "")
        );

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().toString())
        );
        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription())
        );

        priorityColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPriority().toString())
        );

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

        //loadTicketBaseOnRole();
        loadAllTickets();
        setupFilter();
    }

    @FXML
    private void handleSortTickets() {
        String selectedSortOrder = prioritySortChoiceBox.getValue();
        ArrayList<Ticket> tickets;

        if (loggedInEmployee.getRole().equalsIgnoreCase("ServiceDesk")) {
            // ServiceDesk can see all tickets
            if (selectedSortOrder.equals("High to Low")) {
                tickets = ticketService.getTicketsSortedByPriorityDescending();
            } else if (selectedSortOrder.equals("Low to High")) {
                tickets = ticketService.getTicketsSortedByPriorityAscending();
            } else {
                tickets = ticketService.getTicketsWithEmployeeNames(); // Default unsorted
            }
        } else {
            // Regular Employee can see only their tickets
            if (selectedSortOrder.equals("High to Low")) {
                tickets = ticketService.getEmployeeTicketsSortedByPriorityDescending(loggedInEmployee.getId().toString());
            } else if (selectedSortOrder.equals("Low to High")) {
                tickets = ticketService.getEmployeeTicketsSortedByPriorityAscending(loggedInEmployee.getId().toString());
            } else {
                tickets = ticketService.getEmployeeTickets(loggedInEmployee.getId().toString()); // Default unsorted
            }
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
            ticketService.updatePriority(selectedTicket.getId(), selectedTicket.getPriority()); //update only the priority
            //loadTicketBaseOnRole();  // Refresh the table to show the updated priority
            ticketTable.refresh();
            showSuccessAlert("Success", "The ticket priority has been escalated.");
        } else {
            showAlert("Error", "Please select a ticket to escalate.");
        }
    }
    @FXML
    private void handleCloseTicket() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            selectedTicket.setStatus(Status.closed);  // Update status to closed
            //ticketService.updateTicket(selectedTicket.getId(), selectedTicket);
            ticketService.updateStatus(selectedTicket.getId(), selectedTicket.getStatus());  // Update the status in the database
            ticketTable.refresh(); // Refresh table
            showSuccessAlert("Success", "The ticket has been closed successfully.");
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/Tickets/CreateIncidentTicket.fxml"));
            Parent root = fxmlLoader.load();

            // Get the controller of CreateIncident and pass logged-in employee data if needed
            Createincidentticket createIncidentController = fxmlLoader.getController();
            createIncidentController.setLoggedInUsername(loggedInEmployee);

            // Create a new stage for the update ticket screen
            Stage stage = new Stage();
            stage.setTitle("Create Incident");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh ticket table upon returning
            loadAllTickets();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Create Incident screen.");
        }
    }

    @FXML
    public void switchToDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/Dashboard/Dashboard.fxml"));
            Parent root = fxmlLoader.load();
            DashboardController controller = new DashboardController(loggedInEmployee);
            fxmlLoader.setController(controller);

            // Create a new stage for the update ticket screen
            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Dashboard screen.");
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
                stage.showAndWait();
                ticketTable.refresh();
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
        if ((loggedInEmployee != null && "ServiceDesk".equalsIgnoreCase(loggedInEmployee.getRole()) && "admin".equalsIgnoreCase(loggedInEmployee.getPrivileges()))) {
            allTickets = FXCollections.observableArrayList(ticketService.getTicketsWithEmployeeNames());
        }
        else {
            allTickets = FXCollections.observableArrayList(ticketService.getTicketsForCurrentUser(loggedInEmployee.getId().toString()));
        }

       // allTickets = FXCollections.observableArrayList(ticketService.getTicketsWithEmployeeNames());
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

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Fred Individual Functionality
    @FXML
    private void transferTicket(){
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            employeesCB.setVisible(true);
            fillComboBox();
        }
    }

    private void fillComboBox() {
        List<Employee> employees = employeeDAO.getAllEmployees();
        ObservableList<Employee> observableEmployees = FXCollections.observableArrayList(employees);
        employeesCB.setItems(observableEmployees);
    }
}

