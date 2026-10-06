package pxl.be.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pxl.be.departmentservice.domain.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByOrganizationId(Long organizationId);
}