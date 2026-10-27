package pxl.be.departmentservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pxl.be.departmentservice.dto.DepartmentRequest;
import pxl.be.departmentservice.dto.DepartmentResponse;
import pxl.be.departmentservice.dto.EmployeeDto;
import pxl.be.departmentservice.service.DepartmentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTests {

    private static final DepartmentResponse HR =
            new DepartmentResponse(1L, "Human Resources", 10L, null);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Test
    void add_withValidBody_returns201AndTheSavedDepartment() throws Exception {
        when(departmentService.add(any(DepartmentRequest.class))).thenReturn(HR);

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Human Resources\",\"organizationId\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Human Resources"))
                .andExpect(jsonPath("$.employees").doesNotExist());
    }

    @Test
    void add_withBlankName_returns400AndDoesNotInvokeService() throws Exception {
        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"organizationId\":10}"))
                .andExpect(status().isBadRequest());

        verify(departmentService, never()).add(any());
    }

    @Test
    void add_withoutOrganization_returns400AndDoesNotInvokeService() throws Exception {
        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Human Resources\"}"))
                .andExpect(status().isBadRequest());

        verify(departmentService, never()).add(any());
    }

    @Test
    void findById_whenExisting_returns200() throws Exception {
        when(departmentService.findById(1L)).thenReturn(Optional.of(HR));

        mockMvc.perform(get("/api/department/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").value(10));
    }

    @Test
    void findById_whenMissing_returns404() throws Exception {
        when(departmentService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/department/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByOrganization_returns200WithTheDepartments() throws Exception {
        when(departmentService.findByOrganization(10L)).thenReturn(List.of(HR));

        mockMvc.perform(get("/api/department/organization/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Human Resources"));
    }

    @Test
    void findByOrganizationWithEmployees_returnsTheEmployeesInsideTheDepartment() throws Exception {
        DepartmentResponse withEmployees = new DepartmentResponse(1L, "Human Resources", 10L,
                List.of(new EmployeeDto(7L, "Jane", "Doe", "jane@pxl.be", 1L, 10L)));
        when(departmentService.findByOrganizationWithEmployees(10L))
                .thenReturn(List.of(withEmployees));

        mockMvc.perform(get("/api/department/organization/10/with-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employees[0].email").value("jane@pxl.be"));
    }
}
