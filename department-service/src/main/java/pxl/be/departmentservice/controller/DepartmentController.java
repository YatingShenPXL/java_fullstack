package pxl.be.departmentservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pxl.be.departmentservice.domain.Department;
import pxl.be.departmentservice.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // POST /api/departments
    @PostMapping
    public ResponseEntity<Department> add(@RequestBody Department department) {
        Department created = departmentService.add(department);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // GET /api/departments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Department> findById(@PathVariable("id") Long id) {
        return departmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/departments
    @GetMapping
    public ResponseEntity<List<Department>> findAll() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    // GET /api/departments/organization/{organizationId}
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Department>> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        return ResponseEntity.ok(departmentService.findByOrganization(organizationId));
    }

    // GET /api/departments/organization/{organizationId}/with-employees
    @GetMapping("/organization/{organizationId}/with-employees")
    public ResponseEntity<List<Department>> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId) {
        return ResponseEntity.ok(departmentService.findByOrganizationWithEmployees(organizationId));
    }
}
