package com.example.no_sql_project.Model;


public class Employee {
    private String id;
    private String name;
    private String password;
    private String role;
    private String privileges;

    // Constructors
    public Employee() {
    }

    public Employee(String id, String name, String password, String role, String privileges) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.privileges = privileges;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

