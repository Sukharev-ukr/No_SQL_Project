package com.example.no_sql_project.Model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class Ticket {
    ObjectId id;
    String employeeId;
    Priority priority;
    Status status;
    LocalDateTime ticketDate;
    String description;
    Type type;

    // Constructors
    // creating an object that already exists in the database
    public Ticket(ObjectId id,String employeeId,Type type, Priority priority, Status status, LocalDateTime ticketDate, String  description) {
        this.id = id;
        this.description = description;
        this.ticketDate = ticketDate;
        this.status = status;
        this.priority = priority;
        this.type = type;
        this.employeeId = employeeId;
    }
    // creating a new Employee object that doesn't yet exist in the DB
    public Ticket(String employeeId,Type type, Priority priority, Status status, LocalDateTime ticketDate, String  description) {

        this.description = description;
        this.ticketDate = ticketDate;
        this.status = status;
        this.priority = priority;
        this.type = type;
        this.employeeId = employeeId;
        this.type = type;
        this.priority = priority;
    }

    public ObjectId getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(LocalDateTime ticketDate) {
        this.ticketDate = ticketDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Type getType() {return type;}

    public void setType(Type type) {this.type = type;}

    public String getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
