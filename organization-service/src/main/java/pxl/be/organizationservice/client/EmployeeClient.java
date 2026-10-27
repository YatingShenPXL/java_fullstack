package pxl.be.organizationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pxl.be.organizationservice.dto.EmployeeDto;

import java.util.List;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("/api/employee/organization/{organizationId}")
    List<EmployeeDto> findByOrganization(@PathVariable("organizationId") Long organizationId);
}
