package pxl.be.employeeservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import pxl.be.employeeservice.domain.Employee;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class EmployeeRepositoryTests {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Autowired
    private EmployeeRepository repository;

    private Employee employee(String email, Long departmentId, Long organizationId) {
        Employee employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Doe");
        employee.setEmail(email);
        employee.setDepartmentId(departmentId);
        employee.setOrganizationId(organizationId);
        return repository.save(employee);
    }

    @Test
    void save_generatesIdAndPersistsAllColumns() {
        Employee saved = employee("jane.doe@pxl.be", 10L, 100L);

        Employee found = repository.findById(saved.getId()).orElseThrow();

        assertThat(found.getEmail()).isEqualTo("jane.doe@pxl.be");
        assertThat(found.getDepartmentId()).isEqualTo(10L);
        assertThat(found.getOrganizationId()).isEqualTo(100L);
    }

    @Test
    void findByDepartmentId_returnsOnlyEmployeesOfThatDepartment() {
        employee("a@pxl.be", 11L, 100L);
        employee("b@pxl.be", 11L, 100L);
        employee("c@pxl.be", 12L, 100L);

        List<Employee> result = repository.findByDepartmentId(11L);

        assertThat(result).extracting(Employee::getEmail)
                .containsExactlyInAnyOrder("a@pxl.be", "b@pxl.be");
    }

    @Test
    void findByOrganizationId_returnsOnlyEmployeesOfThatOrganization() {
        employee("a@pxl.be", 10L, 201L);
        employee("b@pxl.be", 10L, 202L);

        List<Employee> result = repository.findByOrganizationId(201L);

        assertThat(result).extracting(Employee::getEmail).containsExactly("a@pxl.be");
    }
}
