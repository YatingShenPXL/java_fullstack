package pxl.be.employeeservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pxl.be.employeeservice.domain.Employee;
import pxl.be.employeeservice.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setFirstName("Jane");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("jane.doe@pxl.be");
        testEmployee.setDepartmentId(10L);
        testEmployee.setOrganizationId(100L);
    }

    @Test
    void add_resetsIdToNull_andSavesEmployee() {
        // Arrange: geef mee met een bestaand ID om te valideren dat de service dit wist
        testEmployee.setId(50L);

        when(employeeRepository.save(testEmployee)).thenReturn(testEmployee);

        // Act
        Employee result = employeeService.add(testEmployee);

        // Assert
        assertThat(result.getId()).isNull();
        assertThat(result.getFirstName()).isEqualTo("Jane");
        verify(employeeRepository).save(testEmployee);
    }

    @Test
    void findById_whenFound_returnsOptionalWithEmployee() {
        // Arrange
        Long id = 1L;
        testEmployee.setId(id);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(testEmployee));

        // Act
        Optional<Employee> result = employeeService.findById(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("jane.doe@pxl.be");
        verify(employeeRepository).findById(id);
    }

    @Test
    void findById_whenNotFound_returnsEmptyOptional() {
        // Arrange
        Long id = 99L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Employee> result = employeeService.findById(id);

        // Assert
        assertThat(result).isEmpty();
        verify(employeeRepository).findById(id);
    }

    @Test
    void findAll_returnsListOfEmployees() {
        // Arrange
        List<Employee> employees = List.of(testEmployee);
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testEmployee);
        verify(employeeRepository).findAll();
    }

    @Test
    void findByDepartment_returnsEmployeesForGivenDepartmentId() {
        // Arrange
        Long departmentId = 10L;
        List<Employee> employees = List.of(testEmployee);
        when(employeeRepository.findByDepartmentId(departmentId)).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.findByDepartment(departmentId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepartmentId()).isEqualTo(departmentId);
        verify(employeeRepository).findByDepartmentId(departmentId);
    }

    @Test
    void findByOrganization_returnsEmployeesForGivenOrganizationId() {
        // Arrange
        Long organizationId = 100L;
        List<Employee> employees = List.of(testEmployee);
        when(employeeRepository.findByOrganizationId(organizationId)).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.findByOrganization(organizationId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrganizationId()).isEqualTo(organizationId);
        verify(employeeRepository).findByOrganizationId(organizationId);
    }
}