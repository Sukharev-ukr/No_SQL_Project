package com.example.no_sql_project.Controller;

import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.Model.*;
import com.example.no_sql_project.Service.TicketService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Updateticket {
    @FXML
    private DatePicker datePickerReported;

    @FXML
    private ComboBox<Type> incidentTypeComboBox;

    //@FXML
    //private ComboBox<String> reportedByComboBox;

    @FXML
    private ComboBox<Priority> priorityComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<Status> statusCombobox;

    @FXML
    private Button updateTicketButton;

    private TicketService ticketService = new TicketService();
    private Map<String, ObjectId> userIdMap = new HashMap<>();
    private Ticket currentTicket;
    private Employee loggedInUser;

    public void setLoggedInUsername(Employee employee) {
       this.loggedInUser = employee;
    }

    public void setCurrentTicket(Ticket ticket) {
        this.currentTicket = ticket;
        populateFields(ticket);
    }

    @FXML
    public void initialize() {
        incidentTypeComboBox.getItems().addAll(Type.values());
        priorityComboBox.getItems().addAll(Priority.values());
        statusCombobox.getItems().addAll(Status.values());
        loadEmployees();
    }

    private void loadEmployees() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        ArrayList<Employee> employees = employeeDAO.getAllEmployees();
        for (Employee employee : employees) {
            userIdMap.put(employee.getName(), employee.getId());
            //reportedByComboBox.getItems().add(employee.getName());
        }
    }

    private void populateFields(Ticket ticket) {
        datePickerReported.setValue(ticket.getTicketDate().toLocalDate());
        incidentTypeComboBox.setValue(ticket.getType());
        priorityComboBox.setValue(ticket.getPriority());
        descriptionArea.setText(ticket.getDescription());

        //for (Map.Entry<String, ObjectId> entry : userIdMap.entrySet()) {
           // if (entry.getValue().equals(ticket.getEmployeeId())) {
              //  reportedByComboBox.setValue(entry.getKey());
              //  break;
          //  }
        //}
    }

    private boolean validateInputs() {
        return datePickerReported.getValue() != null &&
                incidentTypeComboBox.getValue() != null &&
                statusCombobox.getValue() != null &&
                priorityComboBox.getValue() != null &&
                !descriptionArea.getText().isEmpty();
    }

    @FXML
    private void handleUpdateTicket() {
        if (validateInputs()) {
            LocalDateTime ticketDate = datePickerReported.getValue().atStartOfDay();
            Priority priority = priorityComboBox.getValue();
            Status status = statusCombobox.getValue();
            Type type = incidentTypeComboBox.getValue();
            String description = descriptionArea.getText();
            //String selectedUser = reportedByComboBox.getValue();
            //ObjectId selectedUserId = userIdMap.get(selectedUser);

            //Update current ticket data
            currentTicket.setTicketDate(ticketDate);
            currentTicket.setPriority(priority);
            currentTicket.setType(type);
            currentTicket.setDescription(description);
            currentTicket.setStatus(status);
            currentTicket.setEmployeeId(loggedInUser.getId().toHexString());

            //ticketService.updateTicket(currentTicket.getId(), currentTicket);
            ticketService.updateTicketDetails(currentTicket.getId(), ticketDate, type, priority, description, status);
            showSuccessAlert("Ticket Updated", "Ticket has been updated successfully.");
            closeWindow();
        } else {
            showAlert("Update Fail", "Please fill in all required fields.");
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

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }
}
