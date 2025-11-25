package com.service;

import com.dto.NotificationRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public String sendNotification(NotificationRequestDTO dto) {

        logger.info("Request notification for account number {}", dto.getAccountNumber());
        logger.info("NOTIFICATION [{}] - {}", dto.getAccountNumber(), dto.getMessage());
        logger.info("Successfully sent notification {}", dto.getMessage());

        return dto.getMessage();
    }
}
