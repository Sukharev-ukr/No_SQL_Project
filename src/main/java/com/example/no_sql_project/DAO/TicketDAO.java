package com.example.no_sql_project.DAO;

import com.example.no_sql_project.Model.Priority;
import com.example.no_sql_project.Model.Status;
import com.example.no_sql_project.Model.Ticket;
import com.example.no_sql_project.Model.Type;
import com.mongodb.DBCallback;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class TicketDAO extends BaseDAO {
    // Name of the collection for Tickets
    private final String COLLECTION_NAME = "Tickets";

    public TicketDAO() {
        collection = database.getCollection(COLLECTION_NAME);
    }

    public void addTicket(Ticket ticket) {
        insertOne(parseDocument(ticket));
    }

    public Ticket findTicketByID(ObjectId id) {
        Document document = new Document("_id", id);
        return parseTicket(findOneQuery(document));
    }

    public ArrayList<Ticket> getAllTickets() {
        FindIterable<Document> ticketCollection = getAll();
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (Document document : ticketCollection) {
            tickets.add(parseTicket(document));
        }
        return tickets;
    }

    public void deleteTicketByID(ObjectId id) {
        deleteOne(id);
    }
    public ArrayList<Ticket> getEmployeeTickets(String employeeID) {
        Document filter = new Document("employeeID", employeeID);
        FindIterable<Document> employeeTickets  = findMultiple(filter);
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (Document document : employeeTickets) {
            tickets.add(parseTicket(document));
        }
        return tickets;
    }

    public void updateTicket(ObjectId id,Ticket ticket) {
        updateOneEntry(id, parseDocument(ticket));
    }


    private Ticket parseTicket(Document data) {

        try {
            return new Ticket(
                    data.getObjectId("_id"),
                    data.getString("Employee_ID"),
                    Ticket.parseType(data.getString("Type")), // Change from Type.valueOf to parseType here
                    Priority.valueOf(data.getString("Priority")),
                    Status.valueOf(data.getString("Status")),
                    LocalDateTime.parse(data.getString("Date")),
                    data.getString("Description"));
        } catch (DateTimeParseException e) {
            System.out.println("failed to convert Document datetime to LocalDateTime");
            System.out.println(e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("failed to convert Document to Ticket object");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    private Document parseDocument(Ticket ticket) {
        Document data = new Document();
        data.put("Employee_ID", ticket.getEmployeeId());
        data.put("Type", ticket.getType().toString()); // Change here
        data.put("Priority", ticket.getPriority().name()); //Add .name() to convert enum constannt
        data.put("Status", ticket.getStatus().name()); //Add .name() to convert enum constannt
        data.put("Date", ticket.getTicketDate().toString()); //convert to standard string
        data.put("Description", ticket.getDescription());
        return data;
    }
}
