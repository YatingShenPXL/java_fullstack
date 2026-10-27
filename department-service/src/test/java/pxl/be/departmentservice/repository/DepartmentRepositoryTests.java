package pxl.be.departmentservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import pxl.be.departmentservice.domain.Department;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class DepartmentRepositoryTests {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Autowired
    private DepartmentRepository repository;

    private Department department(String name, Long organizationId) {
        Department department = new Department();
        department.setName(name);
        department.setOrganizationId(organizationId);
        return repository.save(department);
    }

    @Test
    void save_generatesIdAndPersistsAllColumns() {
        Department saved = department("Human Resources", 10L);

        Department found = repository.findById(saved.getId()).orElseThrow();

        assertThat(found.getName()).isEqualTo("Human Resources");
        assertThat(found.getOrganizationId()).isEqualTo(10L);
    }

    @Test
    void findByOrganizationId_returnsOnlyDepartmentsOfThatOrganization() {
        department("IT", 21L);
        department("HR", 21L);
        department("Finance", 22L);

        List<Department> result = repository.findByOrganizationId(21L);

        assertThat(result).extracting(Department::getName)
                .containsExactlyInAnyOrder("IT", "HR");
    }
}
