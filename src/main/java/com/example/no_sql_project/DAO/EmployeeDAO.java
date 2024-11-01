package com.example.no_sql_project.DAO;

import com.example.no_sql_project.Model.Employee;

import com.example.no_sql_project.utils.PasswordUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;

import com.example.no_sql_project.Model.Ticket;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class EmployeeDAO extends BaseDAO {

    final String DATABASE = "NoSQL_Project";
    private static final String COLLECTION_NAME = "Employees";  // Name of the collection for employees

  public EmployeeDAO() {
      if (mongoDbConnection.getDatabase() == null) {
          mongoDbConnection.setDatabase(DATABASE);
      }
      database = mongoDbConnection.getDatabase();
      mongoDbConnection.setCollection(COLLECTION_NAME);
      collection = database.getCollection(COLLECTION_NAME);
    }


    public Employee findEmployeeById(ObjectId id) {
        Document query = new Document("_id", id);
        Document out = findOneQuery(query);
        return parseEmployee(out);

    }
    public ArrayList<Employee> getAllEmployees(){
        FindIterable<Document> employeeCollection = getAll();
        ArrayList<Employee> allEmployees = new ArrayList<Employee>();
        for (Document document : employeeCollection) {
            allEmployees.add(parseEmployee(document));
        }
        return allEmployees;
    }

    public void addEmployee(Employee employee){
        insertOne(parseDocument(employee));
    }

    public Employee findEmployeeByName(String username) {
        Document filter = new Document("Name", username);  // Assuming "Name" is the field in the MongoDB collection
        Document userDocument = collection.find(filter).first();

        if (userDocument != null) {
            return parseEmployee(userDocument);
        } else {
            return null;  // No employee found with the given username
        }
    }


    public void deleteEmployee(ObjectId id){
        deleteOne(id);
  }
    public Document findEmployeeByNameAndPassword(String name, String plainPassword) {
        // Hash the user's input password to match the database hash
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        // Create the query with hashed password
        Document query = new Document("Name", name).append("Password", hashedPassword);

        // Use the BaseDAO findQuery method to search for the user in the database
        return findOneQuery(query);
    }

    public void updateEmployee(ObjectId id, Employee employee){
        updateOneEntry(id, parseDocument(employee));
    }

    private Employee parseEmployee (Document data){
        try {
            return new Employee(
                    data.getObjectId("_id"),
                    data.getString("Name"),
                    data.getString("Password"),
                    data.getString("Role"),
                    data.getString("Privileges") );
        }
        catch (ClassCastException e){
            System.out.println("failed to convert Document to Employee object");
            System.out.println(e.getMessage());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    private Document parseDocument (Employee employee)
    {
        Document data = new Document();
        data.put("Name", employee.getName());
        data.put("Password", employee.getPassword());
        data.put("Role", employee.getRole());
        data.put("Privileges", employee.getPrivileges());
        return data;
    }

}
