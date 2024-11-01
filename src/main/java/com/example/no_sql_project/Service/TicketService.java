package com.example.no_sql_project.Service;

import com.example.no_sql_project.DAO.TicketDAO;
import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Model.Ticket;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class TicketService {
    private TicketDAO ticketDAO;
    public TicketService() {
        ticketDAO = new TicketDAO();
    }
    public ArrayList<Ticket> getAllTickets() {
        ArrayList<Ticket> tickets = ticketDAO.getAllTickets();
        return tickets;
    }
    public void addTickets(Ticket ticket) {
        ticketDAO.addTicket(ticket);
    }
    public void deleteTicket(ObjectId id) {
        ticketDAO.deleteTicketByID(id);
    }
    public void updateTicket(ObjectId id,Ticket ticket) {
        ticketDAO.updateTicket(id, ticket);
    }
    public ArrayList<Ticket> getEmployeeTickets(String employeeID) {
        ArrayList<Ticket> tickets = ticketDAO.getEmployeeTickets(employeeID);
        return tickets;
    }
    public ArrayList<Ticket> getTicketsSortedByPriorityAscending() {
        return ticketDAO.getAllTicketsSortedByPriorityAscending();
    }

    public ArrayList<Ticket> getTicketsSortedByPriorityDescending() {
        return ticketDAO.getAllTicketsSortedByPriorityDescending();
    }
    public ArrayList<Ticket> getTicketsWithEmployeeNames() {
        ArrayList<Ticket> tickets = ticketDAO.getTicketsWithEmployeeNames();
        return tickets;
    }
}
