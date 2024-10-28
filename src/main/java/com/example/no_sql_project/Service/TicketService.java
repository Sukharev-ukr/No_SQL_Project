package com.example.no_sql_project.Service;

import com.example.no_sql_project.DAO.TicketDAO;
import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Model.Ticket;
import org.bson.types.ObjectId;

public class TicketService {
    private TicketDAO ticketDAO;
    public TicketService() {
        ticketDAO = new TicketDAO();
    }
    public Ticket[] GetAllTickets() {
        Ticket[] tickets = ticketDAO.getAllTickets();
        return tickets;
    }
    public void addTickets(Ticket ticket) {
        ticketDAO.addTicket(ticket);
    }
    public void deleteTicket(ObjectId id) {
        ticketDAO.deleteTicketByID(id);
    }
    public void updateTicket(Ticket ticket) {
        ticketDAO.updateTicket(ticket);
    }
}
