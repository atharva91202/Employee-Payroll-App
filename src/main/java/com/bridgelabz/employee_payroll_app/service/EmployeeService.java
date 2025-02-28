package com.bridgelabz.employee_payroll_app.service;
import com.bridgelabz.employee_payroll_app.exception.EmployeeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import com.bridgelabz.employee_payroll_app.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeService {
    private final List<Employee> employeeList = new ArrayList<>();
    private long idCounter = 1;

    // Create an Employee
    public ResponseEntity<Employee> addEmployee(Employee employee) {
        employee.setId((int) idCounter++);
        employeeList.add(employee);
        log.info("Added Employee : {}",employee);
        return ResponseEntity.ok(employee);
    }

    // Get Employee by ID
    public ResponseEntity<Employee> getEmployeeById(Long id) {
        log.info("Fectching Employee with ID: {}",id);
        Optional<Employee> employee = employeeList.stream()
                .filter(emp -> emp.getId() == id.intValue())
                .findFirst();
        if (employee.isPresent()) {
            log.info("Employee found: {}", employee.get());
            return ResponseEntity.ok(employee.get());
        } else {
            log.warn("Employee with ID {} not found", id);
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
    }

    // Get all Employees
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Fetching all employee. Total count: {}",employeeList.size());
        return ResponseEntity.ok(employeeList);
    }

    // Update Employee
    public ResponseEntity<Employee> updateEmployee(Long id, Employee updatedEmployee) {
        log.info("Updating Employee with ID:{}",id);
        for (Employee emp : employeeList) {
            if (emp.getId() == id.intValue()) {
                emp.setName(updatedEmployee.getName());
                emp.setSalary(updatedEmployee.getSalary());
                log.info("Updated Employee:{}",emp);
                return ResponseEntity.ok(emp);
            }
        }
        log.warn("Employee with ID {} not found for update",id);
        throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
    }

    // Delete Employee
    public ResponseEntity<String> deleteEmployee(Long id) {
        log.info("Deleting Employee with ID:{}",id);
        boolean removed = employeeList.removeIf(emp -> emp.getId() == id.intValue());
        if (removed) {
            log.info("Employee with ID {} deleted successfully",id);
            return ResponseEntity.ok("Employee deleted successfully for ID: " + id);
        }
        log.warn("Employee with ID {} not found for deletion",id);
        throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
    }
}