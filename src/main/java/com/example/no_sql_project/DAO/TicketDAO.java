package com.example.no_sql_project.DAO;

import com.example.no_sql_project.Model.Priority;
import com.example.no_sql_project.Model.Status;
import com.example.no_sql_project.Model.Ticket;
import com.example.no_sql_project.Model.Type;
import com.mongodb.DBCallback;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.print.Doc;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.Arrays;


import static com.mongodb.client.model.Filters.eq;

public class TicketDAO extends BaseDAO {

    final String DATABASE = "NoSQL_Project";
    private final String COLLECTION_NAME = "Tickets";

//    age for a ticket when its eligible to be archived, measured in days
    private final int TICKET_ARCHIVE_DATE = 720;

    public TicketDAO() {
        if (mongoDbConnection.getDatabase() == null) {
            mongoDbConnection.setDatabase(DATABASE);
        }
        database = mongoDbConnection.getDatabase();
        mongoDbConnection.setCollection(COLLECTION_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    public void addTicket(Ticket ticket) {
        insertOne(parseDocument(ticket));
    }

    public Ticket findTicketByID(ObjectId id) {
        Document document = new Document("_id", id);
        return parseTicket(findOneQuery(document));
    }
    public ArrayList<Ticket> getEmployeeTicketsSortedByPriorityAscending(String employeeID) {
        Document filter = new Document("Employee_ID", employeeID);
        FindIterable<Document> ticketCollection = collection.find(filter).sort(ascending("Priority"));
        return parseTicketList(ticketCollection);
    }

    public ArrayList<Ticket> getEmployeeTicketsSortedByPriorityDescending(String employeeID) {
        Document filter = new Document("Employee_ID", employeeID);
        FindIterable<Document> ticketCollection = collection.find(filter).sort(descending("Priority"));
        return parseTicketList(ticketCollection);
    }

    public ArrayList<Ticket> getAllTickets() {
        FindIterable<Document> ticketCollection = getAll();
        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets = parseTicketList(ticketCollection);

        return tickets;
    }
    public ArrayList<Ticket> getAllTicketsSortedByPriorityAscending() {
        FindIterable<Document> ticketCollection = getAll().sort(ascending("Priority"));
        return parseTicketList(ticketCollection);
    }

    public ArrayList<Ticket> getAllTicketsSortedByPriorityDescending() {
        FindIterable<Document> ticketCollection = getAll().sort(descending("Priority"));
        return parseTicketList(ticketCollection);
    }

    public ArrayList<Ticket> getOlderThan(String date) {
        Document filter = new Document("Date",new Document("$lt",date));
        FindIterable<Document> ticketCollection = findMultiple(filter);
        return parseTicketList(ticketCollection);
    }

    public ArrayList<Ticket> archiveTickets(){
        int archiveAmount = 0;
        ArchiveDoa archiveDoa = new ArchiveDoa();
        String cutOffDate = archiveDoa.setCutOffDate(TICKET_ARCHIVE_DATE);

        Document filter = new Document("Date",new Document("$lt",cutOffDate));

        FindIterable<Document> ticketCollection = findMultiple(filter);
        ArrayList<Document> listTickets = new ArrayList<>();
        for (Document document : ticketCollection) { listTickets.add(document); }
        archiveAmount = listTickets.size();
        if (!listTickets.isEmpty()) {
            archiveDoa.insertManyIntoArchive(listTickets);
            deleteMany(filter);
        }

        ArrayList<Ticket> returnTickets = new ArrayList<>();
        for (Document document : listTickets) {returnTickets.add(parseTicket(document));}
        return returnTickets;
    }

    // Utility method to parse tickets from FindIterable
    private ArrayList<Ticket> parseTicketList(FindIterable<Document> ticketCollection) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (Document document : ticketCollection) {
            if (parseTicket(document) != null){
                tickets.add(parseTicket(document));
            }

        }
        return tickets;
    }

    public void deleteTicketByID(ObjectId id) {
        deleteOne(id);
    }

    
    public ArrayList<Ticket> getEmployeeTickets(String employeeID) {
        Document filter = new Document("Employee_ID", employeeID);
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

    public ArrayList<Ticket> getOpenTickets() {
        Document filter = new Document("status", Status.open.toString());
        FindIterable<Document> ticketCollection = findMultiple(filter);

        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets = parseTicketList(ticketCollection);
        return tickets;
    }

    private Ticket parseTicket(Document data) {

        try {
            return new Ticket(
                    data.getObjectId("_id"),
                    data.getString("Employee_ID"), //I change back From ObjectID to String
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


    public ArrayList<Ticket> getTicketsWithEmployeeNames() {
        ArrayList<Ticket> tickets = new ArrayList<>();

        try {
            AggregateIterable<Document> results = collection.aggregate(Arrays.asList(
                    new Document("$lookup", new Document()
                            .append("from", "Employees")
                            .append("let", new Document("id", new Document("$toObjectId", "$Employee_ID")))
                            .append("pipeline", Arrays.asList(
                                    new Document("$match", new Document("$expr", new Document("$eq", Arrays.asList("$_id", "$$id"))))
                            ))
                            .append("as", "Employee")),
                    new Document("$addFields", new Document("name", new Document("$arrayElemAt", Arrays.asList("$Employee.Name", 0))))
            )).maxTime(60000, java.util.concurrent.TimeUnit.MILLISECONDS).allowDiskUse(true);


            for (Document document : results) {
                Ticket ticket = parseTicket(document);
                if (ticket != null){
                    ticket.setEmployeeName(document.getString("name"));
                    tickets.add(ticket);
                }
            }
            return tickets;
        } catch (Exception e) {
            System.err.println("Error during aggregation: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Ticket> getTicketsForCurrentUser(String employeeId) {
        ArrayList<Ticket> tickets = new ArrayList<>();

        try {
            AggregateIterable<Document> results = collection.aggregate(Arrays.asList(
                    new Document("$match", new Document("Employee_ID", employeeId)), // Filter by Employee_ID
                    new Document("$lookup", new Document()
                            .append("from", "Employees")
                            .append("let", new Document("id", new Document("$toObjectId", "$Employee_ID")))
                            .append("pipeline", Arrays.asList(
                                    new Document("$match", new Document("$expr", new Document("$eq", Arrays.asList("$_id", "$$id"))))
                            ))
                            .append("as", "Employee")),
                    new Document("$addFields", new Document("name", new Document("$arrayElemAt", Arrays.asList("$Employee.Name", 0))))
            )).maxTime(60000, java.util.concurrent.TimeUnit.MILLISECONDS).allowDiskUse(true);

            for (Document document : results) {
                Ticket ticket = parseTicket(document);
                ticket.setEmployeeName(document.getString("name"));
                tickets.add(ticket);
            }
        } catch (Exception e) {
            System.err.println("Error during aggregation for current user tickets: " + e.getMessage());
            e.printStackTrace();
        }

        return tickets;
    }

    public void updateTicketPriority(ObjectId ticketId, Priority newPriority) { //for escalation ticket priority
        Document update = new Document("$set", new Document("Priority", newPriority.name())); // Only update Priority
        collection.updateOne(eq("_id", ticketId), update); // Execute update based on ticket ID
    }

    public void updateTicketStatus(ObjectId ticketId, Status newStatus) { // for update ticket status
        Document update = new Document("$set", new Document("Status", newStatus.name()));
        collection.updateOne(eq("_id", ticketId), update);
    }

    public void updateEmployeeTicket(ObjectId ticketId, String newEmployeeid) { // for update ticket employee
        Document update = new Document("$set", new Document("Employee_ID", newEmployeeid));
        collection.updateOne(eq("_id", ticketId), update);
    }


    public void updateTicketDetails(ObjectId ticketId, LocalDateTime date, Type type, Priority priority, String description) {
        Document updateFields = new Document();
        if (date != null) {
            updateFields.put("Date", date.toString());  // Convert LocalDateTime to String for storage
        }
        if (type != null) {
            updateFields.put("Type", type.toString());
        }
        if (priority != null) {
            updateFields.put("Priority", priority.toString());
        }
        if (description != null && !description.isEmpty()) {
            updateFields.put("Description", description);
        }

        // Only proceed if there is at least one field to update
        if (!updateFields.isEmpty()) {
            Document updateQuery = new Document("$set", updateFields);
            collection.updateOne(eq("_id", ticketId), updateQuery);
        }
    }

    private LocalDateTime parseDate(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Date parsing error: " + e.getMessage());
            return LocalDateTime.now();  // Use current date as fallback
        }
    }
}
