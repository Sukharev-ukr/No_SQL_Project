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

    public ArrayList<Ticket> getAllTickets() {
        FindIterable<Document> ticketCollection = getAll();
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (Document document : ticketCollection) {
            tickets.add(parseTicket(document));
        }
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

    public void archiveTickets(){
        ArchiveDoa archiveDoa = new ArchiveDoa();
        String cutOffDate = archiveDoa.setCutOffDate(TICKET_ARCHIVE_DATE);

        Document filter = new Document("Date",new Document("$lt",cutOffDate));

        FindIterable<Document> ticketCollection = findMultiple(filter);
        ArrayList<Document> listTickets = new ArrayList<>();
        for (Document document : ticketCollection) { listTickets.add(document); }
        System.out.println(mongoDbConnection.database.getName());
        if (!listTickets.isEmpty()) {
            archiveDoa.insertManyIntoArchive(listTickets);
            deleteMany(filter);
        }



    }

    // Utility method to parse tickets from FindIterable
    private ArrayList<Ticket> parseTicketList(FindIterable<Document> ticketCollection) {
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
    public ArrayList<Ticket> getOpenTickets() {
        Document filter = new Document("status", Status.open.toString());
        FindIterable<Document> ticketCollection = findMultiple(filter);

        ArrayList<Ticket> tickets = new ArrayList<>();
        for (Document document : ticketCollection) {
            tickets.add(parseTicket(document));
        }
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

        // Aggregation pipeline with $lookup to join Tickets with Employees, excluding _id
        AggregateIterable<Document> results = collection.aggregate(Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "Employees")
                        .append("localField", "Employee_ID")
                        .append("foreignField", "_id")
                        .append("as", "employeeInfo")
                ),
                new Document("$unwind", "$employeeInfo"),  // Flatten the joined array
                new Document("$project", new Document()  // Select the fields you need
                        .append("Type", 1)
                        .append("Priority", 1)
                        .append("Status", 1)
                        .append("Date", 1)
                        .append("Description", 1)
                        .append("employeeName", "$employeeInfo.Name")  // Get Name from employeeInfo
                )
        ));

        // Parse the result into Ticket objects
        for (Document document : results) {
            Ticket ticket = new Ticket(
                    document.getString("employeeName"),  // Employee name from lookup
                    Ticket.parseType(document.getString("Type")),
                    Priority.valueOf(document.getString("Priority")),
                    Status.valueOf(document.getString("Status")),
                    LocalDateTime.parse(document.getString("Date")),
                    document.getString("Description")
            );
            tickets.add(ticket);
        }

        return tickets;
    }
}
