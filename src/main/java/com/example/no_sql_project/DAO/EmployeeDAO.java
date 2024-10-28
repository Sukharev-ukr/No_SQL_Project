package com.example.no_sql_project.DAO;

import com.example.no_sql_project.Model.Employee;
import com.example.no_sql_project.utils.PasswordUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

public class EmployeeDAO extends BaseDAO {

    private static final String COLLECTION_NAME = "Employees";  // Name of the collection for employees



    public Employee findEmployeeByID (ObjectId id) {
        Document document = new Document("_id", id);
        findQuery(COLLECTION_NAME,document);
        return null;


    }

    public Document findEmployeeByNameAndPassword(String name, String plainPassword) {
        // Hash the user's input password to match the database hash
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        // Create the query with hashed password
        Document query = new Document("Name", name).append("Password", hashedPassword);

        // Use the BaseDAO findQuery method to search for the user in the database
        return findQuery("Employees", query);
    }

    public Employee[] getAllEmployees() {
        return null;
    }
    public void deleteEmployeeByID (ObjectId id) {

    }
    public void addEmployee (Employee employee) {

    }
    public void updateEmployee (Employee employee) {}
}
