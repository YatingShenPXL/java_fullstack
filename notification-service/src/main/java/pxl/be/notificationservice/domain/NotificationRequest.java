package pxl.be.notificationservice.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(

        @NotBlank
        @Email
        String recipient,

        @NotBlank
        String message
) {
}
