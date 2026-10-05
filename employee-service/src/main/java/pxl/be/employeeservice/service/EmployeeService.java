package pxl.be.employeeservice.service;

import org.springframework.stereotype.Service;
import pxl.be.employeeservice.domain.Employee;
import pxl.be.employeeservice.dto.EmployeeRequest;
import pxl.be.employeeservice.dto.EmployeeResponse;
import pxl.be.employeeservice.repository.EmployeeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = new Employee();
        mapDtoToEntity(request, employee);

        Employee saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee niet gevonden met id: " + id));
        return mapToResponse(employee);
    }

    public List<EmployeeResponse> getEmployeesByDepartment(long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EmployeeResponse> getEmployeesByOrganization(long organizationId) {
        return employeeRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee niet gevonden met id: " + id));

        mapDtoToEntity(request, employee);

        Employee updated = employeeRepository.save(employee);
        return mapToResponse(updated);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NoSuchElementException("Employee niet gevonden met id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    private void mapDtoToEntity(EmployeeRequest request, Employee employee) {
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setPhone(request.phone());
        employee.setAddress(request.address());
        employee.setCity(request.city());
        employee.setState(request.state());
        employee.setDateOfBirth(request.dateOfBirth());
        employee.setDepartmentId(request.departmentId());
        employee.setOrganizationId(request.organizationId());
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getAddress(),
                employee.getCity(),
                employee.getState(),
                employee.getDateOfBirth(),
                employee.getDepartmentId(),
                employee.getOrganizationId()
        );
    }
}
