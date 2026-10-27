package pxl.be.employeeservice.dto;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long departmentId,
        Long organizationId) { }
