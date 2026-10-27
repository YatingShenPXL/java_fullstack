package pxl.be.departmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentRequest(
        @NotBlank String name,
        @NotNull Long organizationId) { }
