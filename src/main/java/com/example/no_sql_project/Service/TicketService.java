package com.example.no_sql_project.Service;

import com.example.no_sql_project.DAO.TicketDAO;
import com.example.no_sql_project.Model.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
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
    public ArrayList<Ticket> getEmployeeTicketsSortedByPriorityAscending(String employeeID) {
        return ticketDAO.getEmployeeTicketsSortedByPriorityAscending(employeeID);
    }

    public ArrayList<Ticket> getEmployeeTicketsSortedByPriorityDescending(String employeeID) {
        return ticketDAO.getEmployeeTicketsSortedByPriorityDescending(employeeID);
    }


    public ArrayList<Ticket> getOpenTickets() {
        return ticketDAO.getOpenTickets();
    }
    public ArrayList<Ticket> getTicketsWithEmployeeNames() {
        ArrayList<Ticket> tickets = ticketDAO.getTicketsWithEmployeeNames();
        return tickets;
    }
    public ArrayList<Ticket> getTicketsForCurrentUser(String employeeId) {
        ArrayList<Ticket> tickets = ticketDAO.getTicketsForCurrentUser(employeeId);
        return tickets;
    }

    public void updateStatus(ObjectId ticketId, Status newStatus) {
        ticketDAO.updateTicketStatus(ticketId, newStatus);
    }

    public void updatePriority(ObjectId ticketId, Priority newPriority) {
        ticketDAO.updateTicketPriority(ticketId, newPriority);
    }
    public void updateTicketDetails(ObjectId ticketId, LocalDateTime date, Type type, Priority priority, String description) {
        ticketDAO.updateTicketDetails(ticketId, date, type, priority, description);
    }
}
