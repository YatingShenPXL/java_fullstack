package pxl.be.departmentservice.domain;

import jakarta.persistence.*;
import pxl.be.employeeservice.domain.Employee;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long organizationId;

    @Column(nullable = false)
    private String name;

    // Wordt niet in de database opgeslagen, maar gevuld via het endpoint 'with-employees'
    @Transient
    private List<Employee> employees = new ArrayList<>();

    public Department() {}

    public Department(Long organizationId, String name) {
        this.organizationId = organizationId;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}