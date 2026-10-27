package pxl.be.organizationservice.dto;

import jakarta.validation.constraints.NotBlank;

public record OrganizationRequest(@NotBlank String name) { }
