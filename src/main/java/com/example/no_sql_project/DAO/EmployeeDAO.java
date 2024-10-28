package com.example.no_sql_project.DAO;

import com.example.no_sql_project.Model.Employee;

import static com.mongodb.client.model.Filters.eq;

import com.example.no_sql_project.Model.Ticket;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class EmployeeDAO extends BaseDAO {

    private static final String COLLECTION_NAME = "Employees";  // Name of the collection for employees

    public EmployeeDAO() {
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

    public void deleteEmployee(ObjectId id){
        deleteOne(id);
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
