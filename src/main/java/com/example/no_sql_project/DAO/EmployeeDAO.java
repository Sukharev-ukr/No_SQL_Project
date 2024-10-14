package com.example.no_sql_project.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

public class EmployeeDAO extends BaseDAO {

    private static final String COLLECTION_NAME = "Employees";  // Name of the collection for employees

    /**
     * Find an employee by name in the employees collection.
     * @param name the name of the employee to search for
     * @return Document containing the employee data, or null if not found
     */

    public Document test (){
        return findQuery(COLLECTION_NAME);

    }
//    public Document findEmployeeByName(String name) {
//        // Use the inherited method to open the connection and get the database
//
//        MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
//
//        try {
//            // Perform the search by name
//            Document employee = collection.find(eq("Name", name)).first();
//            return employee;  // Return the found document
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;  // Return null in case of an exception
//        } finally {
//            // Optionally, close the client if not needed anymore
//            closeConnection();
//        }
//    }
}
