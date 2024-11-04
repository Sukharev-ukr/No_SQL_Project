package com.example.no_sql_project.Controller;

import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Model.Status;
import com.example.no_sql_project.Model.Ticket;
import com.example.no_sql_project.Service.TicketService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    VBox mainContainer;
    @FXML
    Label openTicketLabel;
    @FXML
    Label urgentTickets;
    @FXML
    Button dashboardButton;
    @FXML
    Button incidentButton;
    @FXML
    Button userButton;
    @FXML
    VBox pastDeadlineBox;
    @FXML
    private HBox navBard;
    @FXML
    private Button showList;
    TicketService ticketService;

    Employee currentUser;
    boolean admin;

    public DashboardController(Employee currentUser) {
        ticketService = new TicketService();

        this.currentUser = currentUser;
//        sets admin bool to if the user that logged in is a service desk employee
        admin = Objects.equals(currentUser.getPrivileges(), "admin");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (currentUser.getPrivileges().equals("Admin")  || currentUser.getPrivileges().equals("admin")) {
            initializeAdmin();
        }else{
            initializeUser();
        }
    }

    @FXML
    public void dashboardClick(){
    }
    @FXML
    public void incidentClick(){
            OverviewTicket controller = new OverviewTicket(currentUser);
            loadFXML("/com/example/no_sql_project/Tickets/OverviewTickets.fxml",controller);
    }
    @FXML
    public void userClick(){
        EmployeeManagementController controller = new EmployeeManagementController(currentUser);
        loadFXML("/com/example/no_sql_project/UserManagement/UserManagement.fxml",controller);

    }

    @FXML
    public void showListClick(){
        OverviewTicket controller = new OverviewTicket(currentUser);
        loadFXML("/com/example/no_sql_project/Tickets/OverviewTickets.fxml",controller);
    }

    private void loadFXML(String path, Object controller){
        try{
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(path));
        fxmlLoader.setController(controller);

            Scene scene = new Scene(fxmlLoader.load());
            Stage currentStage = (Stage) mainContainer.getScene().getWindow();
            currentStage.setScene(scene);
        }catch (IOException e){e.printStackTrace();}
    }
    private void loadFXML(String path){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(path));
            Scene scene = new Scene(fxmlLoader.load());

            Stage currentStage = (Stage) mainContainer.getScene().getWindow();
            currentStage.setScene(scene);
        }catch (IOException e){e.printStackTrace();}
    }

    private void initializeUser(){
        System.out.println("logged in as user");
        showList.setVisible(true);

        ArrayList<Ticket> tickets =  ticketService.getEmployeeTickets(currentUser.getId().toString());
        ArrayList<ArrayList<Ticket>> ticketStates = getTicketStates(tickets);

        openTicketLabel.setText(MessageFormat.format("Open: {0}\nResolved: {1}\nClosed: {2}\nTotal Ticket Count: {3}",
                ticketStates.get(0).size(),ticketStates.get(1).size(),ticketStates.get(0).size(), tickets.size()));
    }
    private void initializeAdmin(){
        System.out.println("logged in as admin");
        navBard.setVisible(true);
        pastDeadlineBox.setVisible(true);

        ArrayList<Ticket> tickets =  ticketService.getAllTickets();
        ArrayList<ArrayList<Ticket>> ticketStates = getTicketStates(tickets);

        openTicketLabel.setText(MessageFormat.format("Open: {0}\nResolved: {1}\nClosed: {2}\nTotal Ticket Count: {3}",
                ticketStates.get(0).size(),ticketStates.get(1).size(),ticketStates.get(0).size(), tickets.size()));
        setUrgentTicketNumber(ticketStates.get(0));

    }
    private void setUrgentTicketNumber(ArrayList<Ticket> openTickets){
        int urgentTicketsCount = 0;
        for (Ticket ticket : openTickets) {
            if ( ChronoUnit.DAYS.between(ticket.getTicketDate(), LocalDateTime.now())>14) {
                urgentTicketsCount++;
            }
        }
        urgentTickets.setText(String.valueOf(urgentTicketsCount));
    }

    private ArrayList<ArrayList<Ticket>> getTicketStates(ArrayList<Ticket> tickets){
        ArrayList<Ticket> openTickets = new ArrayList<>();
        ArrayList<Ticket> closedTickets = new ArrayList<>();
        ArrayList<Ticket> resolvedTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == Status.open){
                openTickets.add(ticket);
            }
            if (ticket.getStatus() == Status.closed){
                closedTickets.add(ticket);
            }
            if (ticket.getStatus() == Status.resolved){

            }
        }
        ArrayList<ArrayList<Ticket>> states = new ArrayList<ArrayList<Ticket>>();
        states.add(openTickets);
        states.add(closedTickets);
        states.add(resolvedTickets);
        return states;
    }
}
