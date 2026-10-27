package pxl.be.employeeservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pxl.be.employeeservice.client.NotificationClient;
import pxl.be.employeeservice.domain.Employee;
import pxl.be.employeeservice.dto.EmployeeRequest;
import pxl.be.employeeservice.dto.EmployeeResponse;
import pxl.be.employeeservice.dto.NotificationRequest;
import pxl.be.employeeservice.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final NotificationClient notificationClient;

    public EmployeeService(EmployeeRepository employeeRepository,
                           NotificationClient notificationClient) {
        this.employeeRepository = employeeRepository;
        this.notificationClient = notificationClient;
    }

    @Transactional
    public EmployeeResponse add(EmployeeRequest request) {
        LOGGER.info("Nieuwe werknemer ontvangen: {} {}",
                request.firstName(), request.lastName());

        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setDepartmentId(request.departmentId());
        employee.setOrganizationId(request.organizationId());

        Employee saved = employeeRepository.save(employee);

        notificationClient.sendNotification(new NotificationRequest(
                saved.getEmail(),
                "Welkom " + saved.getFirstName() + " " + saved.getLastName()));

        return toResponse(saved);
    }

    public Optional<EmployeeResponse> findById(Long id) {
        return employeeRepository.findById(id).map(this::toResponse);
    }

    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<EmployeeResponse> findByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<EmployeeResponse> findByOrganization(Long organizationId) {
        return employeeRepository.findByOrganizationId(organizationId).stream()
                .map(this::toResponse)
                .toList();
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentId(),
                employee.getOrganizationId());
    }
}
