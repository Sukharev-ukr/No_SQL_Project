package com.example.no_sql_project.Model;

import java.time.LocalDateTime;

public class Ticket {
    String id;
    String employeeId;
    int ticketNr;
    Status status;
    LocalDateTime ticketDate;
    String description;
    Type type;
    Priority priority;

    public Ticket(String description, LocalDateTime ticketDate, Status status, int ticketNr, String employeeId, Type type, Priority priority) {
        this.description = description;
        this.ticketDate = ticketDate;
        this.status = status;
        this.ticketNr = ticketNr;
        this.employeeId = employeeId;
        this.type = type;
        this.priority = priority;
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

    public int getTicketNr() {
        return ticketNr;
    }

    public void setTicketNr(int ticketNr) {
        this.ticketNr = ticketNr;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Type getType() {return type;}

    public void setType(Type type) {this.type = type;}

    public Priority getPriority() {return priority;}

    public void setPriority(Priority priority) {this.priority = priority;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
