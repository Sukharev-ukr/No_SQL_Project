package com.example.no_sql_project.Model;

import java.time.LocalDateTime;

public class Ticket {
    String id;
    String employeeId;
    int ticketNr;
    Status status;
    LocalDateTime ticketDate;
    String description;

    public Ticket(String description, LocalDateTime ticketDate, Status status, int ticketNr, String employeeId) {
        this.description = description;
        this.ticketDate = ticketDate;
        this.status = status;
        this.ticketNr = ticketNr;
        this.employeeId = employeeId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
