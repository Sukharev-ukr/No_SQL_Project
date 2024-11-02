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
        if (admin) {
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
        loadFXML("/com/example/no_sql_project/Tickets/OverviewTickets.fxml");
    }
    @FXML
    public void userClick(){
        loadFXML("/com/example/no_sql_project/UserManagement/UserManagement.fxml");
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
        incidentButton.setVisible(true);

        ArrayList<Ticket> tickets =  ticketService.getEmployeeTickets(currentUser.getId().toString());
        ArrayList<Ticket> openTickets = getOpenTickets(tickets);

        setOpenCloseTicketRatio(tickets, openTickets);
    }
    private void initializeAdmin(){
        System.out.println("logged in as admin");
        incidentButton.setVisible(true);
        userButton.setVisible(true);
        pastDeadlineBox.setVisible(true);

        ArrayList<Ticket> tickets =  ticketService.getAllTickets();
        ArrayList<Ticket> openTickets = getOpenTickets(tickets);

        setOpenCloseTicketRatio(tickets, openTickets);
        setUrgentTicketNumber(openTickets);

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
    private void setOpenCloseTicketRatio(ArrayList<Ticket> tickets, ArrayList<Ticket> openTickets){
        openTicketLabel.setText(MessageFormat.format("{0}/{1}",openTickets.size(), tickets.size()));
    }
    private ArrayList<Ticket> getOpenTickets(ArrayList<Ticket> tickets){
        ArrayList<Ticket> openTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == Status.open){
                openTickets.add(ticket);
            }
        }
        return openTickets;
    }
}
