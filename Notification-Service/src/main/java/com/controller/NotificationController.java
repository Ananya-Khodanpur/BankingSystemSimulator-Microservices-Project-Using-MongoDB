package com.controller;

import com.dto.ApiResponse;
import com.dto.NotificationRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.service.NotificationService;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping(value = "send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> sendNotification(
            @Valid @RequestBody NotificationRequestDTO dto) {

        String message = notificationService.sendNotification(dto);
        return ResponseEntity.ok().body(ApiResponse.success(message, null));
    }
}
