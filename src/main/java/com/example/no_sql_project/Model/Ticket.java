package com.example.no_sql_project.Model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class Ticket {
    ObjectId id;
    ObjectId employeeId;
    String employeeName;
    Priority priority;
    Status status;
    LocalDateTime ticketDate;
    String description;
    Type type;

    // Constructors
    // creating an object that already exists in the database
    public Ticket(ObjectId id,ObjectId employeeId,Type type, Priority priority, Status status, LocalDateTime ticketDate, String  description) {
        this.id = id;
        this.description = description;
        this.ticketDate = ticketDate;
        this.status = status;
        this.priority = priority;
        this.type = type;
        this.employeeId = employeeId;
    }
    // creating a new Employee object that doesn't yet exist in the DB
    public Ticket(ObjectId employeeId,Type type, Priority priority, Status status, LocalDateTime ticketDate, String  description) {

        this.description = description;
        this.ticketDate = ticketDate;
        this.status = status;
        this.employeeId = employeeId;
        this.type = type;
        this.priority = priority;
    }
    // Constructor without id
    public Ticket(String employeeName, Type type, Priority priority, Status status, LocalDateTime ticketDate, String description) {
        this.employeeName = employeeName;
        this.type = type;
        this.priority = priority;
        this.status = status;
        this.ticketDate = ticketDate;
        this.description = description;
    }

    public String getEmployeeName() {
        return employeeName;
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

    //public void setType(Type type)
    //{
        //this.type = type;
    //}

    public void setType(Object inputType) {
        if (inputType instanceof Type) { //if inputType is a Type, it assigns it directly.
            this.type = (Type) inputType;
        } else if (inputType instanceof String) {
            this.type = parseType((String) inputType); //If inputType is a String, it uses the parseType helper method to convert the string to the corresponding Type enum.
        } else {
            throw new IllegalArgumentException("Invalid type input: " + inputType);
        }
    }

    public ObjectId getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(ObjectId employeeId) {
        this.employeeId = employeeId;
    }
    // Helper method to convert a string to the appropriate Type enum
    public static Type parseType(String typeString) { //A setType(Object inputType) method that handles both Type enums and strings, converting strings using parseType.
        for (Type type : Type.values()) {
            if (type.toString().equalsIgnoreCase(typeString)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown Type: " + typeString);
    }
}
