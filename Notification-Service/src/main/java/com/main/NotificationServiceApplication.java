package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com")
@EnableDiscoveryClient
public class NotificationServiceApplication {

    static Logger logger = LoggerFactory.getLogger(NotificationServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
        logger.info("Notification service up on port number 8083");
	}

}
