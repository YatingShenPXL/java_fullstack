package pxl.be.departmentservice.service;


import pxl.be.departmentservice.domain.Department;

import java.util.List;
import java.util.Optional;

public interface IDepartmentService {
    Department add(Department department);
    Optional<Department> findById(Long id);
    List<Department> findAll();
    List<Department> findByOrganization(Long organizationId);
    List<Department> findByOrganizationWithEmployees(Long organizationId);
}
