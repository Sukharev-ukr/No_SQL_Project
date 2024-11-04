package com.example.no_sql_project.Service;

import com.example.no_sql_project.DAO.EmployeeDAO;
import com.example.no_sql_project.Model.Employee;
import org.bson.types.ObjectId;

public class EmployeeService {

    EmployeeDAO employeeDAO = new EmployeeDAO();


    public void passDeleteEmployee(ObjectId id) {
        employeeDAO.deleteEmployee(id);
    }

    public void passUpdateEmployee(Employee newEmployee) {
        employeeDAO.addEmployee(newEmployee);
    }

    public void passAddEmployee(Employee newEmployee) {
        employeeDAO.addEmployee(newEmployee);
    }


}
