package com.example.no_sql_project.Controller;


import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Service.EmployeeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class EmployeeManagementController {

    @FXML
    private TableView<Employee> userTable;
    @FXML
    private TableColumn<Employee, String> idColumn;
    @FXML
    private TableColumn<Employee, String> fullNameColumn;
    @FXML
    private TableColumn<Employee, String> roleColumn;
    @FXML
    private Button btnDeleteUser;
    @FXML
    private Button btnUpdateUser;
    @FXML
    private Button incidentButton;

    private Employee loggedInEmployee;

    EmployeeDAO employeeDAO = new EmployeeDAO();
    EmployeeService employeeService = new EmployeeService();

    public EmployeeManagementController(Employee loggedInEmployee){
        this.loggedInEmployee = loggedInEmployee;
    }


    public void initialize() {
        configureTableColumns();
        loadUsers();
    }

    private void configureTableColumns() {

        idColumn.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getId().toString()));

        fullNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole()));

    }

    private void loadUsers() {
        ObservableList<Employee> showingObservableList = FXCollections.observableArrayList(employeeDAO.getAllEmployees());
        userTable.setItems(showingObservableList);
    }

    @FXML
    private void switchToIncidentManagement() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/Tickets/OverviewTickets.fxml"));
           OverviewTicket overviewTicket = new OverviewTicket(loggedInEmployee);
            fxmlLoader.setController(overviewTicket);
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Incident Management");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (OverviewTicket)
            Stage currentStage = (Stage) userTable.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Unable to load the Create Incident screen.");
        }
    }

    @FXML
    private void onTableViewClick(){
        if (userTable.getSelectionModel().getSelectedItem() != null) {
            btnDeleteUser.setDisable(false);
            btnUpdateUser.setDisable(false);
        }
        else {
            btnDeleteUser.setDisable(true);
            btnUpdateUser.setDisable(true);
        }
    }

    @FXML
    public void openAddUser(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/UserManagement/AddUser.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadUsers();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateUser() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/UserManagement/UpdateUser.fxml"));

            UpdateEmployeeController controller = new UpdateEmployeeController(loggedInEmployee);
            fxmlLoader.setController(controller);

            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadUsers();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteUser(){

        boolean confirmed = showDeleteConfirmationDialog();

        if (confirmed) {
            Employee selectedEmployee = userTable.getSelectionModel().getSelectedItem();
            employeeService.passDeleteEmployee(selectedEmployee.getId());
            loadUsers();
        }
    }

    public boolean showDeleteConfirmationDialog() {
        //Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this item?");

        //Display the alert and wait for response
        Optional<ButtonType> result = alert.showAndWait();

        //Check user click
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    public void switchToDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/no_sql_project/Dashboard/Dashboard.fxml"));
            DashboardController controller = new DashboardController(loggedInEmployee);
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            // Create a new stage for the update ticket screen
            Stage stage = (Stage)userTable.getScene().getWindow();
            stage.setTitle("Dashboard");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
