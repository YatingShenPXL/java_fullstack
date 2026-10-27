package pxl.be.organizationservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@Transactional(readOnly = true)
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final DepartmentClient departmentClient;
    private final EmployeeClient employeeClient;

    public OrganizationService(OrganizationRepository organizationRepository,
                               DepartmentClient departmentClient,
                               EmployeeClient employeeClient) {
        this.organizationRepository = organizationRepository;
        this.departmentClient = departmentClient;
        this.employeeClient = employeeClient;
    }

    @Transactional
    public OrganizationResponse add(OrganizationRequest request) {
        Organization organization = new Organization();
        organization.setName(request.name());

        return toResponse(organizationRepository.save(organization), null, null);
    }

    public List<OrganizationResponse> findAll() {
        return organizationRepository.findAll().stream()
                .map(organization -> toResponse(organization, null, null))
                .toList();
    }

    public Optional<OrganizationResponse> findById(Long id) {
        return organizationRepository.findById(id)
                .map(organization -> toResponse(organization, null, null));
    }

    public Optional<OrganizationResponse> findByIdWithDepartments(Long id) {
        return organizationRepository.findById(id)
                .map(organization -> toResponse(organization,
                        departmentClient.findByOrganization(id), null));
    }

    public Optional<OrganizationResponse> findByIdWithEmployees(Long id) {
        return organizationRepository.findById(id)
                .map(organization -> toResponse(organization,
                        null, employeeClient.findByOrganization(id)));
    }

    public Optional<OrganizationResponse> findByIdWithDepartmentsAndEmployees(Long id) {
        return organizationRepository.findById(id)
                .map(organization -> toResponse(organization,
                        departmentClient.findByOrganizationWithEmployees(id), null));
    }

    private OrganizationResponse toResponse(Organization organization,
                                            List<DepartmentDto> departments,
                                            List<EmployeeDto> employees) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                departments,
                employees);
    }
}
