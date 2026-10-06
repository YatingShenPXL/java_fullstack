package pxl.be.organizationservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pxl.be.organizationservice.domain.Organization;
import pxl.be.organizationservice.service.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(
            OrganizationService organizationService) {

        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<Organization> add(
            @Valid @RequestBody Organization organization) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(organizationService.add(organization));
    }

    @GetMapping
    public ResponseEntity<List<Organization>> findAll() {
        return ResponseEntity.ok(organizationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> findById(
            @PathVariable("id") Long id) {

        return organizationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-departments")
    public ResponseEntity<Organization> findByIdWithDepartments(
            @PathVariable("id") Long id) {

        return organizationService.findByIdWithDepartments(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-employees")
    public ResponseEntity<Organization> findByIdWithEmployees(
            @PathVariable("id") Long id) {

        return organizationService.findByIdWithEmployees(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-departments-and-employees")
    public ResponseEntity<Organization> findByIdWithDepartmentsAndEmployees(
            @PathVariable("id") Long id) {

        return organizationService.findByIdWithDepartmentsAndEmployees(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}