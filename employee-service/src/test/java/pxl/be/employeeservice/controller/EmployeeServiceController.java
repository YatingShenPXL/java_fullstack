package pxl.be.employeeservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pxl.be.employeeservice.domain.Employee;
import pxl.be.employeeservice.service.EmployeeService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTests {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("Jane");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("jane.doe@pxl.be");
        testEmployee.setDepartmentId(10L);
        testEmployee.setOrganizationId(100L);
    }

    @Test
    void add_returnsCreatedStatusAndSavedEmployee() {
        // Arrange
        Employee input = new Employee();
        input.setFirstName("Jane");
        input.setLastName("Doe");
        input.setEmail("jane.doe@pxl.be");
        input.setDepartmentId(10L);
        input.setOrganizationId(100L);

        when(employeeService.add(input)).thenReturn(testEmployee);

        // Act
        ResponseEntity<Employee> response = employeeController.add(input);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getFirstName()).isEqualTo("Jane");
        assertThat(response.getBody().getLastName()).isEqualTo("Doe");
        assertThat(response.getBody().getEmail()).isEqualTo("jane.doe@pxl.be");
        verify(employeeService).add(input);
    }

    @Test
    void findById_whenFound_returnsOkStatusAndEmployee() {
        // Arrange
        Long id = 1L;
        when(employeeService.findById(id)).thenReturn(Optional.of(testEmployee));

        // Act
        ResponseEntity<Employee> response = employeeController.findById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testEmployee);
        assertThat(response.getBody().getEmail()).isEqualTo("jane.doe@pxl.be");
        verify(employeeService).findById(id);
    }

    @Test
    void findById_whenNotFound_returnsNotFoundStatus() {
        // Arrange
        Long id = 99L;
        when(employeeService.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Employee> response = employeeController.findById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(employeeService).findById(id);
    }

    @Test
    void findAll_returnsOkStatusAndEmployeeList() {
        // Arrange
        List<Employee> employees = List.of(testEmployee);
        when(employeeService.findAll()).thenReturn(employees);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.findAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testEmployee);
        verify(employeeService).findAll();
    }

    @Test
    void findByDepartment_returnsOkStatusAndEmployeeList() {
        // Arrange
        Long departmentId = 10L;
        List<Employee> employees = List.of(testEmployee);
        when(employeeService.findByDepartment(departmentId)).thenReturn(employees);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.findByDepartment(departmentId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testEmployee);
        assertThat(response.getBody().get(0).getDepartmentId()).isEqualTo(departmentId);
        verify(employeeService).findByDepartment(departmentId);
    }

    @Test
    void findByOrganization_returnsOkStatusAndEmployeeList() {
        // Arrange
        Long organizationId = 100L;
        List<Employee> employees = List.of(testEmployee);
        when(employeeService.findByOrganization(organizationId)).thenReturn(employees);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.findByOrganization(organizationId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testEmployee);
        assertThat(response.getBody().get(0).getOrganizationId()).isEqualTo(organizationId);
        verify(employeeService).findByOrganization(organizationId);
    }
}