package pxl.be.organizationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pxl.be.organizationservice.client.DepartmentClient;
import pxl.be.organizationservice.client.EmployeeClient;
import pxl.be.organizationservice.domain.Organization;
import pxl.be.organizationservice.dto.DepartmentDto;
import pxl.be.organizationservice.dto.EmployeeDto;
import pxl.be.organizationservice.dto.OrganizationRequest;
import pxl.be.organizationservice.dto.OrganizationResponse;
import pxl.be.organizationservice.repository.OrganizationRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTests {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private DepartmentClient departmentClient;

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private OrganizationService organizationService;

    private Organization organization(Long id, String name) {
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);
        return organization;
    }

    @Test
    void add_savesOrganizationWithTheRequestedName() {
        when(organizationRepository.save(any(Organization.class)))
                .thenReturn(organization(1L, "PXL"));

        OrganizationResponse response = organizationService.add(new OrganizationRequest("PXL"));

        ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
        verify(organizationRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getName()).isEqualTo("PXL");
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.departments()).isNull();
        assertThat(response.employees()).isNull();
    }

    @Test
    void findById_whenNotFound_returnsEmptyAndCallsNoOtherService() {
        when(organizationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(organizationService.findById(99L)).isEmpty();
        verifyNoInteractions(departmentClient, employeeClient);
    }

    @Test
    void findByIdWithDepartments_fillsDepartmentsFromDepartmentService() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization(1L, "PXL")));
        when(departmentClient.findByOrganization(1L))
                .thenReturn(List.of(new DepartmentDto(5L, "IT", 1L, null)));

        OrganizationResponse response = organizationService.findByIdWithDepartments(1L).orElseThrow();

        assertThat(response.departments()).extracting(DepartmentDto::name).containsExactly("IT");
        assertThat(response.employees()).isNull();
        verifyNoInteractions(employeeClient);
    }

    @Test
    void findByIdWithEmployees_fillsEmployeesFromEmployeeService() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization(1L, "PXL")));
        when(employeeClient.findByOrganization(1L))
                .thenReturn(List.of(new EmployeeDto(7L, "Jane", "Doe", "jane@pxl.be", 5L, 1L)));

        OrganizationResponse response = organizationService.findByIdWithEmployees(1L).orElseThrow();

        assertThat(response.employees()).extracting(EmployeeDto::email).containsExactly("jane@pxl.be");
        assertThat(response.departments()).isNull();
        verifyNoInteractions(departmentClient);
    }

    @Test
    void findByIdWithDepartmentsAndEmployees_returnsDepartmentsThatContainTheirEmployees() {
        EmployeeDto jane = new EmployeeDto(7L, "Jane", "Doe", "jane@pxl.be", 5L, 1L);
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization(1L, "PXL")));
        when(departmentClient.findByOrganizationWithEmployees(1L))
                .thenReturn(List.of(new DepartmentDto(5L, "IT", 1L, List.of(jane))));

        OrganizationResponse response =
                organizationService.findByIdWithDepartmentsAndEmployees(1L).orElseThrow();

        assertThat(response.departments()).hasSize(1);
        assertThat(response.departments().get(0).employees()).containsExactly(jane);
    }
}
