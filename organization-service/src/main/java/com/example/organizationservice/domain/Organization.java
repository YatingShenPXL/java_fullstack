package com.example.organizationservice.domain;


import jakarta.persistence.*;
import pxl.be.departmentservice.domain.Department;
import pxl.be.employeeservice.domain.Employee;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    @Transient
    private List<Department> departments = new ArrayList<>();

    @Transient
    private List<Employee> employees = new ArrayList<>();

    public Organization() {}

    public Organization(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<Department> getDepartments() { return departments; }
    public void setDepartments(List<Department> departments) { this.departments = departments; }
    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}
