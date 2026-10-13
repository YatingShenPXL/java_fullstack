package pxl.be.departmentservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pxl.be.departmentservice.domain.Department;
import pxl.be.departmentservice.service.DepartmentService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTests {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Human Resources");
        testDepartment.setOrganizationId(10L);
    }

    @Test
    void add_returnsCreatedStatusAndSavedDepartment() {
        // Arrange
        Department input = new Department();
        input.setName("Human Resources");
        input.setOrganizationId(10L);

        when(departmentService.add(input)).thenReturn(testDepartment);

        // Act
        ResponseEntity<Department> response = departmentController.add(input);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Human Resources");
        verify(departmentService).add(input);
    }

    @Test
    void findById_whenFound_returnsOkStatusAndDepartment() {
        // Arrange
        Long id = 1L;
        when(departmentService.findById(id)).thenReturn(Optional.of(testDepartment));

        // Act
        ResponseEntity<Department> response = departmentController.findById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testDepartment);
        verify(departmentService).findById(id);
    }

    @Test
    void findById_whenNotFound_returnsNotFoundStatus() {
        // Arrange
        Long id = 99L;
        when(departmentService.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Department> response = departmentController.findById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(departmentService).findById(id);
    }

    @Test
    void findAll_returnsOkStatusAndDepartmentList() {
        // Arrange
        List<Department> departments = List.of(testDepartment);
        when(departmentService.findAll()).thenReturn(departments);

        // Act
        ResponseEntity<List<Department>> response = departmentController.findAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testDepartment);
        verify(departmentService).findAll();
    }

    @Test
    void findByOrganization_returnsOkStatusAndDepartmentList() {
        // Arrange
        Long organizationId = 10L;
        List<Department> departments = List.of(testDepartment);
        when(departmentService.findByOrganization(organizationId)).thenReturn(departments);

        // Act
        ResponseEntity<List<Department>> response = departmentController.findByOrganization(organizationId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testDepartment);
        verify(departmentService).findByOrganization(organizationId);
    }

    @Test
    void findByOrganizationWithEmployees_returnsOkStatusAndDepartmentList() {
        // Arrange
        Long organizationId = 10L;
        List<Department> departments = List.of(testDepartment);
        when(departmentService.findByOrganizationWithEmployees(organizationId)).thenReturn(departments);

        // Act
        ResponseEntity<List<Department>> response = departmentController.findByOrganizationWithEmployees(organizationId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testDepartment);
        verify(departmentService).findByOrganizationWithEmployees(organizationId);
    }
}