package pxl.be.organizationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrganizationResponse(
        Long id,
        String name,
        List<DepartmentDto> departments,
        List<EmployeeDto> employees) { }
