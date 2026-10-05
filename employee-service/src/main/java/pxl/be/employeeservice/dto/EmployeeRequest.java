package pxl.be.employeeservice.dto;

import java.time.LocalDateTime;

public record EmployeeRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String city,
        String state,
        LocalDateTime dateOfBirth,
        long departmentId,
        long organizationId) { }
