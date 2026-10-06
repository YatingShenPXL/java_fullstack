package pxl.be.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pxl.be.notificationservice.domain.NotificationRequest;

@Service
public class NotificationService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(NotificationService.class);

    public void notify(NotificationRequest request) {
        LOGGER.info(
                "Gesimuleerde notificatie voor {}: {}",
                request.recipient(),
                request.message()
        );
    }
}