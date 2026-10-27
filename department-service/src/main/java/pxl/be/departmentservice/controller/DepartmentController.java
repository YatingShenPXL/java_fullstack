package pxl.be.departmentservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pxl.be.departmentservice.dto.DepartmentRequest;
import pxl.be.departmentservice.dto.DepartmentResponse;
import pxl.be.departmentservice.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> add(
            @Valid @RequestBody DepartmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.add(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> findById(
            @PathVariable("id") Long id) {

        return departmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> findAll() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<DepartmentResponse>> findByOrganization(
            @PathVariable("organizationId") Long organizationId) {

        return ResponseEntity.ok(
                departmentService.findByOrganization(organizationId));
    }

    @GetMapping("/organization/{organizationId}/with-employees")
    public ResponseEntity<List<DepartmentResponse>> findByOrganizationWithEmployees(
            @PathVariable("organizationId") Long organizationId) {

        return ResponseEntity.ok(
                departmentService.findByOrganizationWithEmployees(organizationId));
    }
}
