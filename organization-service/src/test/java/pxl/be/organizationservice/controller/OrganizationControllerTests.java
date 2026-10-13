package pxl.be.organizationservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pxl.be.organizationservice.domain.Organization;
import pxl.be.organizationservice.service.OrganizationService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTests {

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationController organizationController;

    private Organization testOrganization;

    @BeforeEach
    void setUp() {
        testOrganization = new Organization();
        testOrganization.setId(1L);
        testOrganization.setName("PXL");
    }

    @Test
    void add_returnsCreatedStatusAndSavedOrganization() {
        // Arrange
        Organization input = new Organization();
        input.setName("PXL");

        when(organizationService.add(input)).thenReturn(testOrganization);

        // Act
        ResponseEntity<Organization> response = organizationController.add(input);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("PXL");
        verify(organizationService).add(input);
    }

    @Test
    void findAll_returnsOkStatusAndOrganizationList() {
        // Arrange
        List<Organization> list = List.of(testOrganization);
        when(organizationService.findAll()).thenReturn(list);

        // Act
        ResponseEntity<List<Organization>> response = organizationController.findAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(testOrganization);
        verify(organizationService).findAll();
    }

    @Test
    void findById_whenFound_returnsOkStatusAndOrganization() {
        // Arrange
        Long id = 1L;
        when(organizationService.findById(id)).thenReturn(Optional.of(testOrganization));

        // Act
        ResponseEntity<Organization> response = organizationController.findById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testOrganization);
        verify(organizationService).findById(id);
    }

    @Test
    void findById_whenNotFound_returnsNotFoundStatus() {
        // Arrange
        Long id = 99L;
        when(organizationService.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Organization> response = organizationController.findById(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(organizationService).findById(id);
    }

    @Test
    void findByIdWithDepartments_whenFound_returnsOkStatus() {
        // Arrange
        Long id = 1L;
        when(organizationService.findByIdWithDepartments(id)).thenReturn(Optional.of(testOrganization));

        // Act
        ResponseEntity<Organization> response = organizationController.findByIdWithDepartments(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testOrganization);
        verify(organizationService).findByIdWithDepartments(id);
    }

    @Test
    void findByIdWithDepartments_whenNotFound_returnsNotFoundStatus() {
        // Arrange
        Long id = 99L;
        when(organizationService.findByIdWithDepartments(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Organization> response = organizationController.findByIdWithDepartments(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(organizationService).findByIdWithDepartments(id);
    }

    @Test
    void findByIdWithEmployees_whenFound_returnsOkStatus() {
        // Arrange
        Long id = 1L;
        when(organizationService.findByIdWithEmployees(id)).thenReturn(Optional.of(testOrganization));

        // Act
        ResponseEntity<Organization> response = organizationController.findByIdWithEmployees(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testOrganization);
        verify(organizationService).findByIdWithEmployees(id);
    }

    @Test
    void findByIdWithEmployees_whenNotFound_returnsNotFoundStatus() {
        // Arrange
        Long id = 99L;
        when(organizationService.findByIdWithEmployees(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Organization> response = organizationController.findByIdWithEmployees(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(organizationService).findByIdWithEmployees(id);
    }

    @Test
    void findByIdWithDepartmentsAndEmployees_whenFound_returnsOkStatus() {
        // Arrange
        Long id = 1L;
        when(organizationService.findByIdWithDepartmentsAndEmployees(id))
                .thenReturn(Optional.of(testOrganization));

        // Act
        ResponseEntity<Organization> response =
                organizationController.findByIdWithDepartmentsAndEmployees(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testOrganization);
        verify(organizationService).findByIdWithDepartmentsAndEmployees(id);
    }

    @Test
    void findByIdWithDepartmentsAndEmployees_whenNotFound_returnsNotFoundStatus() {
        // Arrange
        Long id = 99L;
        when(organizationService.findByIdWithDepartmentsAndEmployees(id))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<Organization> response =
                organizationController.findByIdWithDepartmentsAndEmployees(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(organizationService).findByIdWithDepartmentsAndEmployees(id);
    }
}