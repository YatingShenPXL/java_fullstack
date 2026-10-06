package pxl.be.employeeservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pxl.be.employeeservice.domain.Employee;
import pxl.be.employeeservice.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> add(
            @Valid @RequestBody Employee employee) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.add(employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(
            @PathVariable("id") Long id) {

        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Employee>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Employee>> findByDepartment(
            @PathVariable("departmentId") Long departmentId) {

        return ResponseEntity.ok(
                employeeService.findByDepartment(departmentId));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Employee>> findByOrganization(
            @PathVariable("organizationId") Long organizationId) {

        return ResponseEntity.ok(
                employeeService.findByOrganization(organizationId));
    }
}