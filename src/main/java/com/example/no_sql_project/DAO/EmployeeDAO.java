package com.example.no_sql_project.DAO;

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

    public Document findEmplyeeByID (ObjectId id) {
        Document document = new Document("_id", id);
        return findQuery(COLLECTION_NAME,document);

    }
    public void testEmployeeDao(String test){
        Document document = new Document();
        createEntry(COLLECTION_NAME,document);
    }
}
