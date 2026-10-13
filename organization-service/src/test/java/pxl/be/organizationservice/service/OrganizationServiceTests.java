package pxl.be.organizationservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pxl.be.departmentservice.domain.Department;
import pxl.be.employeeservice.domain.Employee;
import pxl.be.organizationservice.domain.Organization;
import pxl.be.organizationservice.repository.OrganizationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTests {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationService organizationService;

    private Organization testOrganization;

    @BeforeEach
    void setUp() {
        testOrganization = new Organization();
        testOrganization.setName("PXL Research");
    }

    @Test
    void add_resetsIdAndCollections_andSavesOrganization() {
        // Arrange
        testOrganization.setId(99L);
        testOrganization.setDepartments(new ArrayList<>(List.of(new Department())));
        testOrganization.setEmployees(new ArrayList<>(List.of(new Employee())));

        when(organizationRepository.save(testOrganization)).thenReturn(testOrganization);

        // Act
        Organization result = organizationService.add(testOrganization);

        // Assert
        assertThat(result.getId()).isNull();
        assertThat(result.getDepartments()).isEmpty();
        assertThat(result.getEmployees()).isEmpty();
        verify(organizationRepository).save(testOrganization);
    }

    @Test
    void findAll_returnsListOfOrganizations() {
        // Arrange
        List<Organization> list = List.of(testOrganization);
        when(organizationRepository.findAll()).thenReturn(list);

        // Act
        List<Organization> result = organizationService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).contains(testOrganization);
        verify(organizationRepository).findAll();
    }

    @Test
    void findById_whenFound_returnsOptionalWithOrganization() {
        // Arrange
        Long id = 1L;
        testOrganization.setId(id);
        when(organizationRepository.findById(id)).thenReturn(Optional.of(testOrganization));

        // Act
        Optional<Organization> result = organizationService.findById(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("PXL Research");
        verify(organizationRepository).findById(id);
    }

    @Test
    void findById_whenNotFound_returnsEmptyOptional() {
        // Arrange
        Long id = 99L;
        when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Organization> result = organizationService.findById(id);

        // Assert
        assertThat(result).isEmpty();
        verify(organizationRepository).findById(id);
    }

    @Test
    void findByIdWithDepartments_whenFound_resetsDepartmentsToEmptyList() {
        // Arrange
        Long id = 1L;
        testOrganization.setId(id);
        testOrganization.setDepartments(new ArrayList<>(List.of(new Department())));
        when(organizationRepository.findById(id)).thenReturn(Optional.of(testOrganization));

        // Act
        Optional<Organization> result = organizationService.findByIdWithDepartments(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getDepartments()).isEmpty();
        verify(organizationRepository).findById(id);
    }

    @Test
    void findByIdWithDepartments_whenNotFound_returnsEmptyOptional() {
        // Arrange
        Long id = 99L;
        when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Organization> result = organizationService.findByIdWithDepartments(id);

        // Assert
        assertThat(result).isEmpty();
        verify(organizationRepository).findById(id);
    }

    @Test
    void findByIdWithEmployees_whenFound_resetsEmployeesToEmptyList() {
        // Arrange
        Long id = 1L;
        testOrganization.setId(id);
        testOrganization.setEmployees(new ArrayList<>(List.of(new Employee())));
        when(organizationRepository.findById(id)).thenReturn(Optional.of(testOrganization));

        // Act
        Optional<Organization> result = organizationService.findByIdWithEmployees(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmployees()).isEmpty();
        verify(organizationRepository).findById(id);
    }

    @Test
    void findByIdWithEmployees_whenNotFound_returnsEmptyOptional() {
        // Arrange
        Long id = 99L;
        when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Organization> result = organizationService.findByIdWithEmployees(id);

        // Assert
        assertThat(result).isEmpty();
        verify(organizationRepository).findById(id);
    }

    @Test
    void findByIdWithDepartmentsAndEmployees_whenFound_resetsBothCollections() {
        // Arrange
        Long id = 1L;
        testOrganization.setId(id);
        testOrganization.setDepartments(new ArrayList<>(List.of(new Department())));
        testOrganization.setEmployees(new ArrayList<>(List.of(new Employee())));
        when(organizationRepository.findById(id)).thenReturn(Optional.of(testOrganization));

        // Act
        Optional<Organization> result = organizationService.findByIdWithDepartmentsAndEmployees(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getDepartments()).isEmpty();
        assertThat(result.get().getEmployees()).isEmpty();
        verify(organizationRepository).findById(id);
    }

    @Test
    void findByIdWithDepartmentsAndEmployees_whenNotFound_returnsEmptyOptional() {
        // Arrange
        Long id = 99L;
        when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Organization> result = organizationService.findByIdWithDepartmentsAndEmployees(id);

        // Assert
        assertThat(result).isEmpty();
        verify(organizationRepository).findById(id);
    }
}