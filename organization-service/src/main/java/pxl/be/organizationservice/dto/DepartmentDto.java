package pxl.be.organizationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DepartmentDto(
        Long id,
        String name,
        Long organizationId,
        List<EmployeeDto> employees) { }
