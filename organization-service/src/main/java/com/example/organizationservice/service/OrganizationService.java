package com.example.organizationservice.service;

import com.example.organizationservice.domain.Organization;
import com.example.organizationservice.dto.OrganizationRequest;
import com.example.organizationservice.dto.OrganizationResponse;
import com.example.organizationservice.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public OrganizationResponse createOrganization(OrganizationRequest request) {
        Organization organization = new Organization(request.name(), request.address());
        Organization saved = organizationRepository.save(organization);
        return mapToResponse(saved);
    }

    public List<OrganizationResponse> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrganizationResponse getOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Organization niet gevonden met id: " + id));
        return mapToResponse(organization);
    }

    public OrganizationResponse updateOrganization(Long id, OrganizationRequest request) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Organization niet gevonden met id: " + id));

        organization.setName(request.name());
        organization.setAddress(request.address());

        Organization updated = organizationRepository.save(organization);
        return mapToResponse(updated);
    }

    public void deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new NoSuchElementException("Organization niet gevonden met id: " + id);
        }
        organizationRepository.deleteById(id);
    }

    public OrganizationResponse mapToResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getAddress(),
                organization.getDepartments(),
                organization.getEmployees()
        );
    }
}
