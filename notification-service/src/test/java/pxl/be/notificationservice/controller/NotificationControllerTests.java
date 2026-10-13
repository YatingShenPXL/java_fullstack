package pxl.be.notificationservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pxl.be.notificationservice.domain.NotificationRequest;
import pxl.be.notificationservice.service.NotificationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTests {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void notify_invokesService_andReturnsCreatedStatusWithoutBody() {
        // Arrange: gebruik de canonical record constructor
        NotificationRequest request = new NotificationRequest("test@pxl.be", "Nieuwe melding");

        // Act
        ResponseEntity<Void> response = notificationController.notify(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNull();
        verify(notificationService).notify(request);
    }
}