package pxl.be.departmentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pxl.be.departmentservice.domain.Department;
import pxl.be.departmentservice.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Department add(Department department) {
        department.setId(null);
        department.setEmployees(List.of());

        return departmentRepository.save(department);
    }

    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public List<Department> findByOrganization(Long organizationId) {
        return departmentRepository.findByOrganizationId(organizationId);
    }

    public List<Department> findByOrganizationWithEmployees(
            Long organizationId) {

        List<Department> departments =
                departmentRepository.findByOrganizationId(organizationId);

        for (Department department : departments) {
            department.setEmployees(List.of());
        }

        return departments;
    }
}