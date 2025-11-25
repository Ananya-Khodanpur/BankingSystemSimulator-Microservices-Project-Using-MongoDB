package com.controller;

import com.dto.ApiResponse;
import com.dto.NotificationRequestDTO;
import com.service.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {
    @Mock
    NotificationService notificationService;

    @InjectMocks
    NotificationController notificationController;

    @Test
    void testSendNotification() {
        NotificationRequestDTO dto =
                new NotificationRequestDTO("ACCXX123", "Successfully Deposited");

        Mockito.when(notificationService.sendNotification(dto))
                .thenReturn("Successfully Deposited");

        ResponseEntity<ApiResponse<?>> response =
                notificationController.sendNotification(dto);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isSuccess());
        Assertions.assertEquals("Successfully Deposited", response.getBody().getMessage());
    }
}
