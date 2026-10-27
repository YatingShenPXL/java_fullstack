package pxl.be.notificationservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pxl.be.notificationservice.domain.NotificationRequest;
import pxl.be.notificationservice.service.NotificationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void notify_withValidBody_returns201AndInvokesService() throws Exception {
        mockMvc.perform(post("/api/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\":\"test@pxl.be\",\"message\":\"Nieuwe melding\"}"))
                .andExpect(status().isCreated());

        verify(notificationService).notify(new NotificationRequest("test@pxl.be", "Nieuwe melding"));
    }

    @Test
    void notify_withInvalidEmail_returns400AndDoesNotInvokeService() throws Exception {
        mockMvc.perform(post("/api/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\":\"geen-email\",\"message\":\"Nieuwe melding\"}"))
                .andExpect(status().isBadRequest());

        verify(notificationService, never()).notify(any());
    }

    @Test
    void notify_withBlankMessage_returns400AndDoesNotInvokeService() throws Exception {
        mockMvc.perform(post("/api/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\":\"test@pxl.be\",\"message\":\"\"}"))
                .andExpect(status().isBadRequest());

        verify(notificationService, never()).notify(any());
    }
}
