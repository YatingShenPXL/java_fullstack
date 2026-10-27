package pxl.be.employeeservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pxl.be.employeeservice.dto.NotificationRequest;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notification")
    void sendNotification(@RequestBody NotificationRequest request);
}
