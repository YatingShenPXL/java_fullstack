package pxl.be.employeeservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pxl.be.employeeservice.dto.EmployeeRequest;
import pxl.be.employeeservice.dto.EmployeeResponse;
import pxl.be.employeeservice.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> add(
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.add(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(
            @PathVariable("id") Long id) {

        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> findByDepartment(
            @PathVariable("departmentId") Long departmentId) {

        return ResponseEntity.ok(
                employeeService.findByDepartment(departmentId));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<EmployeeResponse>> findByOrganization(
            @PathVariable("organizationId") Long organizationId) {

        return ResponseEntity.ok(
                employeeService.findByOrganization(organizationId));
    }
}
