package pxl.be.notificationservice.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import pxl.be.notificationservice.domain.NotificationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NotificationServiceTests {

    private NotificationService notificationService;
    private ListAppender<ILoggingEvent> listAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();

        // Koppel een in-memory appender aan de logger van de service
        logger = (Logger) LoggerFactory.getLogger(NotificationService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(listAppender);
    }

    @Test
    void notify_executesSuccessfullyWithoutExceptions() {
        // Arrange (uitgaande van een Java record NotificationRequest)
        NotificationRequest request = new NotificationRequest("test@pxl.be", "Welkom bij PXL!");

        // Act & Assert
        assertDoesNotThrow(() -> notificationService.notify(request));
    }

    @Test
    void notify_logsCorrectMessageAndLevel() {
        // Arrange
        String recipient = "student@pxl.be";
        String message = "Nieuwe afdeling toegevoegd";
        NotificationRequest request = new NotificationRequest(recipient, message);

        // Act
        notificationService.notify(request);

        // Assert
        assertThat(listAppender.list).hasSize(1);

        ILoggingEvent loggingEvent = listAppender.list.get(0);
        assertThat(loggingEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(loggingEvent.getFormattedMessage())
                .isEqualTo("Notificatie ontvangen voor student@pxl.be: Nieuwe afdeling toegevoegd");
    }
}