package pxl.be.employeeservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pxl.be.employeeservice.client.NotificationClient;
import pxl.be.employeeservice.domain.Employee;
import pxl.be.employeeservice.dto.EmployeeRequest;
import pxl.be.employeeservice.dto.EmployeeResponse;
import pxl.be.employeeservice.dto.NotificationRequest;
import pxl.be.employeeservice.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee(Long id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName("Jane");
        employee.setLastName("Doe");
        employee.setEmail("jane.doe@pxl.be");
        employee.setDepartmentId(10L);
        employee.setOrganizationId(100L);
        return employee;
    }

    @Test
    void add_savesEmployeeWithTheRequestedFields() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee(1L));
        EmployeeRequest request =
                new EmployeeRequest("Jane", "Doe", "jane.doe@pxl.be", 10L, 100L);

        EmployeeResponse response = employeeService.add(request);

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        Employee saved = captor.getValue();
        assertThat(saved.getId()).isNull();
        assertThat(saved.getFirstName()).isEqualTo("Jane");
        assertThat(saved.getLastName()).isEqualTo("Doe");
        assertThat(saved.getEmail()).isEqualTo("jane.doe@pxl.be");
        assertThat(saved.getDepartmentId()).isEqualTo(10L);
        assertThat(saved.getOrganizationId()).isEqualTo(100L);
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void add_sendsNotificationToTheEmailOfTheSavedEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee(1L));

        employeeService.add(new EmployeeRequest("Jane", "Doe", "jane.doe@pxl.be", 10L, 100L));

        ArgumentCaptor<NotificationRequest> captor =
                ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationClient).sendNotification(captor.capture());
        assertThat(captor.getValue().recipient()).isEqualTo("jane.doe@pxl.be");
        assertThat(captor.getValue().message()).contains("Jane", "Doe");
    }

    @Test
    void findById_whenFound_mapsAllFields() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee(1L)));

        Optional<EmployeeResponse> result = employeeService.findById(1L);

        assertThat(result).contains(new EmployeeResponse(
                1L, "Jane", "Doe", "jane.doe@pxl.be", 10L, 100L));
    }

    @Test
    void findById_whenNotFound_returnsEmpty() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(employeeService.findById(99L)).isEmpty();
    }

    @Test
    void findByDepartment_returnsOnlyEmployeesOfThatDepartment() {
        when(employeeRepository.findByDepartmentId(10L))
                .thenReturn(List.of(employee(1L), employee(2L)));

        List<EmployeeResponse> result = employeeService.findByDepartment(10L);

        assertThat(result).extracting(EmployeeResponse::id).containsExactly(1L, 2L);
        assertThat(result).allMatch(response -> response.departmentId().equals(10L));
    }

    @Test
    void findByOrganization_returnsOnlyEmployeesOfThatOrganization() {
        when(employeeRepository.findByOrganizationId(100L))
                .thenReturn(List.of(employee(3L)));

        List<EmployeeResponse> result = employeeService.findByOrganization(100L);

        assertThat(result).extracting(EmployeeResponse::id).containsExactly(3L);
        assertThat(result.get(0).organizationId()).isEqualTo(100L);
    }
}
