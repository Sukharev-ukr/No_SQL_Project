package com.example.no_sql_project.DAO;

import com.example.no_sql_project.Model.Employee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

public class EmployeeDAO extends BaseDAO {

    private static final String COLLECTION_NAME = "Employees";  // Name of the collection for employees

    /**
     * Find an employee by name in the employees' collection.
     * @param name the name of the employee to search for
     * @return Document containing the employee data, or null if not found
     */

    public Employee findEmployeeByID (ObjectId id) {
        Document document = new Document("_id", id);
        findQuery(COLLECTION_NAME,document);
        return null;


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
