package pxl.be.departmentservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pxl.be.departmentservice.client.EmployeeClient;
import pxl.be.departmentservice.domain.Department;
import pxl.be.departmentservice.dto.DepartmentRequest;
import pxl.be.departmentservice.dto.DepartmentResponse;
import pxl.be.departmentservice.dto.EmployeeDto;
import pxl.be.departmentservice.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department(Long id, String name, Long organizationId) {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        department.setOrganizationId(organizationId);
        return department;
    }

    private EmployeeDto employee(Long id, Long departmentId) {
        return new EmployeeDto(id, "Jane", "Doe", "jane" + id + "@pxl.be", departmentId, 1L);
    }

    @Test
    void add_savesDepartmentWithTheRequestedFields() {
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(department(5L, "Human Resources", 1L));

        DepartmentResponse response =
                departmentService.add(new DepartmentRequest("Human Resources", 1L));

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
        verify(departmentRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getName()).isEqualTo("Human Resources");
        assertThat(captor.getValue().getOrganizationId()).isEqualTo(1L);
        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.employees()).isNull();
    }

    @Test
    void findById_whenNotFound_returnsEmpty() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(departmentService.findById(99L)).isEmpty();
    }

    @Test
    void findByOrganization_doesNotCallEmployeeService() {
        when(departmentRepository.findByOrganizationId(1L))
                .thenReturn(List.of(department(1L, "IT", 1L)));

        List<DepartmentResponse> result = departmentService.findByOrganization(1L);

        assertThat(result).extracting(DepartmentResponse::name).containsExactly("IT");
        verifyNoInteractions(employeeClient);
    }

    @Test
    void findByOrganizationWithEmployees_groupsEmployeesPerDepartment() {
        when(departmentRepository.findByOrganizationId(1L))
                .thenReturn(List.of(department(1L, "IT", 1L), department(2L, "HR", 1L)));
        when(employeeClient.findByOrganization(1L))
                .thenReturn(List.of(employee(10L, 1L), employee(11L, 1L), employee(12L, 2L)));

        List<DepartmentResponse> result = departmentService.findByOrganizationWithEmployees(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).employees()).extracting(EmployeeDto::id).containsExactly(10L, 11L);
        assertThat(result.get(1).employees()).extracting(EmployeeDto::id).containsExactly(12L);
    }

    @Test
    void findByOrganizationWithEmployees_departmentWithoutEmployeesGetsEmptyList() {
        when(departmentRepository.findByOrganizationId(1L))
                .thenReturn(List.of(department(1L, "IT", 1L)));
        when(employeeClient.findByOrganization(1L)).thenReturn(List.of());

        List<DepartmentResponse> result = departmentService.findByOrganizationWithEmployees(1L);

        assertThat(result.get(0).employees()).isNotNull().isEmpty();
    }
}
