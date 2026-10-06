package pxl.be.organizationservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pxl.be.organizationservice.domain.Organization;
import pxl.be.organizationservice.repository.OrganizationRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(
            OrganizationRepository organizationRepository) {

        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public Organization add(Organization organization) {
        organization.setId(null);
        organization.setDepartments(List.of());
        organization.setEmployees(List.of());

        return organizationRepository.save(organization);
    }

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }

    public Optional<Organization> findByIdWithDepartments(Long id) {
        return organizationRepository.findById(id)
                .map(organization -> {
                    organization.setDepartments(List.of());
                    return organization;
                });
    }

    public Optional<Organization> findByIdWithEmployees(Long id) {
        return organizationRepository.findById(id)
                .map(organization -> {
                    organization.setEmployees(List.of());
                    return organization;
                });
    }

    public Optional<Organization> findByIdWithDepartmentsAndEmployees(
            Long id) {

        return organizationRepository.findById(id)
                .map(organization -> {
                    organization.setDepartments(List.of());
                    organization.setEmployees(List.of());
                    return organization;
                });
    }
}