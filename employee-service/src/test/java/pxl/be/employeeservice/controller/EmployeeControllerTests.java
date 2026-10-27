package pxl.be.employeeservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pxl.be.employeeservice.dto.EmployeeRequest;
import pxl.be.employeeservice.dto.EmployeeResponse;
import pxl.be.employeeservice.service.EmployeeService;

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

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTests {

    private static final EmployeeResponse JANE =
            new EmployeeResponse(1L, "Jane", "Doe", "jane.doe@pxl.be", 10L, 100L);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void add_withValidBody_returns201AndTheSavedEmployee() throws Exception {
        when(employeeService.add(any(EmployeeRequest.class))).thenReturn(JANE);

        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Jane","lastName":"Doe","email":"jane.doe@pxl.be",
                                 "departmentId":10,"organizationId":100}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("jane.doe@pxl.be"));
    }

    @Test
    void add_withInvalidBody_returns400AndDoesNotInvokeService() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"","lastName":"Doe","email":"geen-email",
                                 "departmentId":10,"organizationId":100}
                                """))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).add(any());
    }

    @Test
    void add_withoutDepartment_returns400AndDoesNotInvokeService() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Jane","lastName":"Doe","email":"jane.doe@pxl.be",
                                 "organizationId":100}
                                """))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).add(any());
    }

    @Test
    void findById_whenExisting_returns200() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.of(JANE));

        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void findById_whenMissing_returns404() throws Exception {
        when(employeeService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employee/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByDepartment_returns200WithTheEmployees() throws Exception {
        when(employeeService.findByDepartment(10L)).thenReturn(List.of(JANE));

        mockMvc.perform(get("/api/employee/department/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].departmentId").value(10));
    }

    @Test
    void findByOrganization_returns200WithTheEmployees() throws Exception {
        when(employeeService.findByOrganization(100L)).thenReturn(List.of(JANE));

        mockMvc.perform(get("/api/employee/organization/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organizationId").value(100));
    }
}
