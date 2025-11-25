package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGateWayApplication {

    static Logger logger = LoggerFactory.getLogger(ApiGateWayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ApiGateWayApplication.class, args);

        logger.info("ApiGateWay is up on port number 8080");

	}

    @Bean
    public RouteLocator getRouteLocator(RouteLocatorBuilder builder){
        return builder
                .routes()
                .route(r->r.path("/api/accounts/**")
                        .and().method("GET","POST","PUT","DELETE")
//                        .uri("http://localhost:8081"))
                          .uri("http://ACCOUNT-SERVICE:8081"))

                .route(r->r.path("/api/transactions/**")
                        .and().method("GET","POST","PUT","DELETE")
//                        .uri("http://localhost:8082"))
                          .uri("http://TRANSACTION-SERVICE:8082"))

                .route(r->r.path("/api/notifications/**")
                        .and().method("GET","POST")
//                        .uri("http://localhost:8083"))
                          .uri("http://NOTIFICATION-SERVICE:8083"))
                .build();
    }

}
