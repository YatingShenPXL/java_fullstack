package pxl.be.organizationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pxl.be.organizationservice.dto.DepartmentDto;

import java.util.List;

@FeignClient(name = "department-service")
public interface DepartmentClient {

    @GetMapping("/api/department/organization/{organizationId}")
    List<DepartmentDto> findByOrganization(@PathVariable("organizationId") Long organizationId);

    @GetMapping("/api/department/organization/{organizationId}/with-employees")
    List<DepartmentDto> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId);
}
