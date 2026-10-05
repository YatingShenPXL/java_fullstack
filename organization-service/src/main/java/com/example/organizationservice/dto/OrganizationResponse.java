package com.example.organizationservice.dto;

import pxl.be.departmentservice.domain.Department;
import pxl.be.employeeservice.domain.Employee;

import java.util.List;

public record OrganizationResponse(
        Long id,
        String name,
        String address,
        List<Department> departments,
        List<Employee> employees) {
}
