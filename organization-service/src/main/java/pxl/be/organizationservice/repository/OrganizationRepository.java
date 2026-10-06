package pxl.be.organizationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pxl.be.organizationservice.domain.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}