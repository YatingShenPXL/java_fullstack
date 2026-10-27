package pxl.be.organizationservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import pxl.be.organizationservice.domain.Organization;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class OrganizationRepositoryTests {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Autowired
    private OrganizationRepository repository;

    @Test
    void save_generatesIdAndPersistsTheName() {
        Organization organization = new Organization();
        organization.setName("PXL");

        Organization saved = repository.save(organization);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId()).orElseThrow().getName()).isEqualTo("PXL");
    }

    @Test
    void findAll_returnsEveryStoredOrganization() {
        Organization first = new Organization();
        first.setName("Alpha");
        Organization second = new Organization();
        second.setName("Beta");
        repository.save(first);
        repository.save(second);

        assertThat(repository.findAll()).extracting(Organization::getName)
                .contains("Alpha", "Beta");
    }
}
