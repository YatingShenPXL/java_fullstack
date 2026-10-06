package pxl.be.notificationservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pxl.be.notificationservice.domain.NotificationRequest;
import pxl.be.notificationservice.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> notify(
            @Valid @RequestBody NotificationRequest request) {

        notificationService.notify(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}