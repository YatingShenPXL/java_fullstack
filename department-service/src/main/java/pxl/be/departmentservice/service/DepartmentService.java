package pxl.be.departmentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pxl.be.departmentservice.client.EmployeeClient;
import pxl.be.departmentservice.domain.Department;
import pxl.be.departmentservice.dto.DepartmentRequest;
import pxl.be.departmentservice.dto.DepartmentResponse;
import pxl.be.departmentservice.dto.EmployeeDto;
import pxl.be.departmentservice.repository.DepartmentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeClient employeeClient;

    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeClient employeeClient) {
        this.departmentRepository = departmentRepository;
        this.employeeClient = employeeClient;
    }

    @Transactional
    public DepartmentResponse add(DepartmentRequest request) {
        Department department = new Department();
        department.setName(request.name());
        department.setOrganizationId(request.organizationId());

        return toResponse(departmentRepository.save(department), null);
    }

    public Optional<DepartmentResponse> findById(Long id) {
        return departmentRepository.findById(id)
                .map(department -> toResponse(department, null));
    }

    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAll().stream()
                .map(department -> toResponse(department, null))
                .toList();
    }

    public List<DepartmentResponse> findByOrganization(Long organizationId) {
        return departmentRepository.findByOrganizationId(organizationId).stream()
                .map(department -> toResponse(department, null))
                .toList();
    }

    public List<DepartmentResponse> findByOrganizationWithEmployees(
            Long organizationId) {

        List<Department> departments =
                departmentRepository.findByOrganizationId(organizationId);

        Map<Long, List<EmployeeDto>> employeesByDepartment =
                employeeClient.findByOrganization(organizationId).stream()
                        .collect(Collectors.groupingBy(EmployeeDto::departmentId));

        return departments.stream()
                .map(department -> toResponse(department,
                        employeesByDepartment.getOrDefault(department.getId(), List.of())))
                .toList();
    }

    private DepartmentResponse toResponse(Department department,
                                          List<EmployeeDto> employees) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getOrganizationId(),
                employees);
    }
}
