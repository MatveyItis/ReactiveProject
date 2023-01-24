package com.griddynamics.reactive.course.api;

import com.griddynamics.reactive.course.api.config.ExternalServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableConfigurationProperties({ExternalServiceProperties.class})
@EnableReactiveMongoRepositories
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
