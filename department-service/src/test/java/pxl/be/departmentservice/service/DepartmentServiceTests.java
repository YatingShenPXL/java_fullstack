package pxl.be.departmentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pxl.be.departmentservice.domain.Department;
import pxl.be.departmentservice.repository.DepartmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setName("Human Resources");
        testDepartment.setOrganizationId(1L);
    }

    @Test
    void add_resetsIdAndEmployees_andSavesDepartment() {
        // Arrange
        testDepartment.setId(99L); // ID die overschreven moet worden
        testDepartment.setEmployees(new ArrayList<>()); // Wordt leeggemaakt

        when(departmentRepository.save(testDepartment)).thenReturn(testDepartment);

        // Act
        Department result = departmentService.add(testDepartment);

        // Assert
        assertThat(result.getId()).isNull();
        assertThat(result.getEmployees()).isEmpty();
        verify(departmentRepository).save(testDepartment);
    }

    @Test
    void findById_whenFound_returnsOptionalWithDepartment() {
        // Arrange
        Long id = 1L;
        testDepartment.setId(id);
        when(departmentRepository.findById(id)).thenReturn(Optional.of(testDepartment));

        // Act
        Optional<Department> result = departmentService.findById(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Human Resources");
        verify(departmentRepository).findById(id);
    }

    @Test
    void findById_whenNotFound_returnsEmptyOptional() {
        // Arrange
        Long id = 99L;
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Department> result = departmentService.findById(id);

        // Assert
        assertThat(result).isEmpty();
        verify(departmentRepository).findById(id);
    }

    @Test
    void findAll_returnsListOfDepartments() {
        // Arrange
        List<Department> departments = List.of(testDepartment);
        when(departmentRepository.findAll()).thenReturn(departments);

        // Act
        List<Department> result = departmentService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testDepartment);
        verify(departmentRepository).findAll();
    }

    @Test
    void findByOrganization_returnsDepartmentsForGivenOrganizationId() {
        // Arrange
        Long organizationId = 1L;
        List<Department> departments = List.of(testDepartment);
        when(departmentRepository.findByOrganizationId(organizationId)).thenReturn(departments);

        // Act
        List<Department> result = departmentService.findByOrganization(organizationId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrganizationId()).isEqualTo(organizationId);
        verify(departmentRepository).findByOrganizationId(organizationId);
    }

    @Test
    void findByOrganizationWithEmployees_resetsEmployeesToEmptyList() {
        // Arrange
        Long organizationId = 1L;
        Department deptWithEmployees = new Department();
        deptWithEmployees.setName("IT");
        deptWithEmployees.setOrganizationId(organizationId);
        // We simuleren dat er initieel werknemers aanwezig waren
        deptWithEmployees.setEmployees(new ArrayList<>());

        when(departmentRepository.findByOrganizationId(organizationId))
                .thenReturn(List.of(deptWithEmployees));

        // Act
        List<Department> result = departmentService.findByOrganizationWithEmployees(organizationId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmployees()).isEmpty();
        verify(departmentRepository).findByOrganizationId(organizationId);
    }
}