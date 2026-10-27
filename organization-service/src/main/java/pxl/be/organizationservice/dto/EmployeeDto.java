package pxl.be.organizationservice.dto;

public record EmployeeDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long departmentId,
        Long organizationId) { }
