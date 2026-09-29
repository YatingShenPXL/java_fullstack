package pxl.be.departmentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import pxl.be.departmentservice.domain.Department;
import pxl.be.departmentservice.repository.DepartmentRepository;
import pxl.be.employeeservice.domain.Employee;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DepartmentService implements IDepartmentService {

    private final DepartmentRepository departmentRepository;
    private final RestClient restClient;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    @Override
    @Transactional
    public Department add(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public List<Department> findByOrganization(Long organizationId) {
        return departmentRepository.findByOrganizationId(organizationId);
    }

    @Override
    public List<Department> findByOrganizationWithEmployees(Long organizationId) {
        List<Department> departments = departmentRepository.findByOrganizationId(organizationId);

        // Haal voor elke afdeling de bijbehorende medewerkers op bij employee-service
        for (Department dept : departments) {
            try {
                Employee[] employees = restClient.get()
                        .uri("/api/employees/department/{departmentId}", dept.getId())
                        .retrieve()
                        .body(Employee[].class);

                if (employees != null) {
                    dept.setEmployees(List.of(employees));
                }
            } catch (Exception e) {
                // Foutafhandeling indien employee-service niet bereikbaar is
                dept.setEmployees(List.of());
            }
        }
        return departments;
    }
}