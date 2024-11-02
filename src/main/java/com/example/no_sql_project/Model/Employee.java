package com.example.no_sql_project.Model;


import org.bson.types.ObjectId;

public class Employee {
    private ObjectId id;
    private String name;
    private String password;
    private String role;
    private String privileges;

    // Constructors
    // creating an object that already exists in the database
    public Employee(ObjectId id, String name, String password, String role, String privileges) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.privileges = privileges;
    }
    // creating a new Employee object that doesn't yet exist in the DB
    public Employee( String name, String password, String role, String privileges) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.privileges = privileges;
    }


    public ObjectId getId(){
        return id;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }
}

