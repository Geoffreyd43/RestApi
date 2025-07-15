package com.example.spring_boot.controller;

import java.util.List;

import com.example.spring_boot.data.Employee;
import com.example.spring_boot.data.EmployeeRepository;
import com.example.spring_boot.exception.EmployeeNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/employees")
    List<Employee> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/employees")
    List<Employee> newEmployee(@RequestBody List<Employee> newEmployees) {
        return repository.saveAll(newEmployees);
    }

    // Single item

    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id.toString()));
    }

    @GetMapping("/employees/name")
    List<Employee> findEmployeeByName(@RequestParam(value="name") String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new EmployeeNotFoundException(name));
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    if (newEmployee.getName() != null) {
                        employee.setName(newEmployee.getName());
                    }
                    if (newEmployee.getRole() != null) {
                        employee.setRole(newEmployee.getRole());
                    }
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}