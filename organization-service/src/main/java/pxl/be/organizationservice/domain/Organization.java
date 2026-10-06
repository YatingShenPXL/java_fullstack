package pxl.be.organizationservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Object> departments = new ArrayList<>();

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Object> employees = new ArrayList<>();
}