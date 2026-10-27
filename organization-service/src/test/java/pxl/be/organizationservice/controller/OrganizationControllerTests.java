package pxl.be.organizationservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pxl.be.organizationservice.dto.DepartmentDto;
import pxl.be.organizationservice.dto.OrganizationRequest;
import pxl.be.organizationservice.dto.OrganizationResponse;
import pxl.be.organizationservice.service.OrganizationService;

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

@WebMvcTest(OrganizationController.class)
class OrganizationControllerTests {

    private static final OrganizationResponse PXL =
            new OrganizationResponse(1L, "PXL", null, null);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrganizationService organizationService;

    @Test
    void add_withValidBody_returns201AndTheSavedOrganization() throws Exception {
        when(organizationService.add(any(OrganizationRequest.class))).thenReturn(PXL);

        mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"PXL\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("PXL"));
    }

    @Test
    void add_withBlankName_returns400AndDoesNotInvokeService() throws Exception {
        mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());

        verify(organizationService, never()).add(any());
    }

    @Test
    void findById_whenExisting_returns200() throws Exception {
        when(organizationService.findById(1L)).thenReturn(Optional.of(PXL));

        mockMvc.perform(get("/api/organization/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PXL"));
    }

    @Test
    void findById_whenMissing_returns404() throws Exception {
        when(organizationService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/organization/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdWithDepartments_returnsTheDepartments() throws Exception {
        OrganizationResponse withDepartments = new OrganizationResponse(1L, "PXL",
                List.of(new DepartmentDto(5L, "IT", 1L, null)), null);
        when(organizationService.findByIdWithDepartments(1L)).thenReturn(Optional.of(withDepartments));

        mockMvc.perform(get("/api/organization/1/with-departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departments[0].name").value("IT"));
    }

    @Test
    void findByIdWithDepartmentsAndEmployees_whenMissing_returns404() throws Exception {
        when(organizationService.findByIdWithDepartmentsAndEmployees(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/organization/99/with-departments-and-employees"))
                .andExpect(status().isNotFound());
    }
}
