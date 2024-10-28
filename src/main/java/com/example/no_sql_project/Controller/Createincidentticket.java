package com.example.no_sql_project.Controller;
import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.Model.*;

import com.example.no_sql_project.Model.Type;
import com.example.no_sql_project.Service.TicketService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Createincidentticket {
    @FXML
    private DatePicker datePickerReported;

    @FXML
    private TextField subjectField;

    @FXML
    private ComboBox<Type> incidentTypeComboBox;

    @FXML
    private ComboBox<String> reportedByComboBox;

    @FXML
    private ComboBox<Priority> priorityComboBox;

    @FXML
    private ComboBox<String> deadlineComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button cancelButton;

    @FXML
    private Button submitTicketButton;
    private TicketService ticketService = new TicketService();
    private Map<String, ObjectId> userIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        incidentTypeComboBox.getItems().addAll(Type.values());
        priorityComboBox.getItems().addAll(Priority.values());

        loadEmployees();


    }

    private void loadEmployees() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee[] employees = employeeDAO.getAllEmployees();
         for (Employee employee : employees) {
             userIdMap.put(employee.getName(), new ObjectId(employee.getId()));
             reportedByComboBox.getItems().add(employee.getName());
         }
    }

    private boolean validateInputs() {
        return datePickerReported.getValue() != null &&
                incidentTypeComboBox.getValue() != null &&
                reportedByComboBox.getValue() != null &&
                priorityComboBox.getValue() != null &&
                !descriptionArea.getText().isEmpty();
    }

    public void handleSubmitTicket() {
        if (validateInputs()) {
            String reportedBy = reportedByComboBox.getValue();
            ObjectId employeeId = userIdMap.get(reportedBy);
            LocalDateTime ticketDate = datePickerReported.getValue().atStartOfDay();
            //something to do here
        }
        else {

        }
    }
}
