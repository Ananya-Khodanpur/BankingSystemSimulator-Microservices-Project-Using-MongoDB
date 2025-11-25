package com.service;

import com.dto.NotificationRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ExtendWith(MockitoExtension.class)
@SpringBootApplication(scanBasePackages = "com")
public class NotificationServiceTest {
    NotificationService notificationService = new NotificationService();

    @Test
    void testSendNotification() {

        NotificationRequestDTO dto =
                new NotificationRequestDTO("ACCXX123", "Successfully Deposited");

        String result = notificationService.sendNotification(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Successfully Deposited", result);
    }
}
