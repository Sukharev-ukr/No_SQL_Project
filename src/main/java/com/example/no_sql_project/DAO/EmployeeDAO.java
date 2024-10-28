package com.example.no_sql_project.DAO;

import com.example.no_sql_project.Model.Employee;
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

    public Document findEmployeeByNameAndPassword(String name, String password) {
        try {
            Document query = new Document("Name", name).append("Password", password);
            return findQuery("Employees", query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
