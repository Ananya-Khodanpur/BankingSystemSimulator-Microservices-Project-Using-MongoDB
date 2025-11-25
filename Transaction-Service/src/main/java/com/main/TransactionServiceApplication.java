package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = "com")
@EnableMongoRepositories(basePackages = "com.repository")
@EnableDiscoveryClient
public class TransactionServiceApplication {

    static Logger logger = LoggerFactory.getLogger(TransactionServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
        logger.info("Transaction service is up on port number 8082");
	}

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate rest = new RestTemplate();
        rest.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });
        return rest;
    }

}
