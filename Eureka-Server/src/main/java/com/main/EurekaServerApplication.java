package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    static Logger logger = LoggerFactory.getLogger(EurekaServerApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);

        logger.info("Eureka server application is up on port number 8761");

	}

}
