package pxl.be.departmentservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DepartmentResponse(
        Long id,
        String name,
        Long organizationId,
        List<EmployeeDto> employees) { }
