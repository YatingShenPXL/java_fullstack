package pxl.be.organizationservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pxl.be.organizationservice.dto.OrganizationRequest;
import pxl.be.organizationservice.dto.OrganizationResponse;
import pxl.be.organizationservice.service.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<OrganizationResponse> add(
            @Valid @RequestBody OrganizationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(organizationService.add(request));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> findAll() {
        return ResponseEntity.ok(organizationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(
            @PathVariable("id") Long id) {

        return organizationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-departments")
    public ResponseEntity<OrganizationResponse> findByIdWithDepartments(
            @PathVariable("id") Long id) {

        return organizationService.findByIdWithDepartments(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-employees")
    public ResponseEntity<OrganizationResponse> findByIdWithEmployees(
            @PathVariable("id") Long id) {

        return organizationService.findByIdWithEmployees(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-departments-and-employees")
    public ResponseEntity<OrganizationResponse> findByIdWithDepartmentsAndEmployees(
            @PathVariable("id") Long id) {

        return organizationService.findByIdWithDepartmentsAndEmployees(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
