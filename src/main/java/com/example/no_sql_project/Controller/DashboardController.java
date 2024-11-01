package com.example.no_sql_project.Controller;

import com.example.no_sql_project.HelloApplication;
import com.example.no_sql_project.Model.Ticket;
import com.example.no_sql_project.Service.TicketService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    VBox mainContainer;
    @FXML
    Label openTicketLabel;
    @FXML
    Label urgentTickets;
    TicketService ticketService;

    public DashboardController() {
        ticketService = new TicketService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Ticket> openTickets = ticketService.getOpenTickets();
        int ticketCount = ticketService.getAllTickets().size();
        int openTicketCount = openTickets.size();
        openTicketLabel.setText(MessageFormat.format("{0}/{1}",openTicketCount, ticketCount));

        int urgentTicketsCount = 0;
        for (Ticket ticket : openTickets) {
            if ( ChronoUnit.DAYS.between(ticket.getTicketDate(), LocalDateTime.now())>14) {
                urgentTicketsCount++;
            }
        }
        urgentTickets.setText(String.valueOf(urgentTicketsCount));
    }

    @FXML
    public void dashboardClick(){
        loadFXML("/com/example/no_sql_project/UserManagement/UserManagement.fxml");
    }
    @FXML
    public void incidentClick(){
        loadFXML("/com/example/no_sql_project/Tickets/OverviewTickets.fxml");
    }
    @FXML
    public void userClick(){
        loadFXML("/com/example/no_sql_project/Tickets/OverviewTickets.fxml");
    }
    private void loadFXML(String path){
        try{
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(path));
        Scene scene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) mainContainer.getScene().getWindow();
        currentStage.setScene(scene);
        }catch (IOException e){e.printStackTrace();}
    }
}
