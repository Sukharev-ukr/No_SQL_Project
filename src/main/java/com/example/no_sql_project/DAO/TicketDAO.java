package com.example.no_sql_project.DAO;

import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.Model.Ticket;
import org.bson.Document;
import org.bson.types.ObjectId;

public class TicketDAO extends BaseDAO {
    private static final String COLLECTION_NAME = "Tickets";  // Name of the collection for employees

    /**
     * Find an employee by name in the employees' collection.
     * @param name the name of the employee to search for
     * @return Document containing the employee data, or null if not found
     */

    public Ticket findTicketByID (ObjectId id) {
        Document document = new Document("_id", id);
        findQuery(COLLECTION_NAME,document);
        return null;


    }
    public Ticket[] getAllTickets() {
        return null;
    }
    public void deleteTicketByID (ObjectId id) {

    }
    public void addTicket (Employee employee) {

    }
    public void updateTicket (Employee employee) {}
}
