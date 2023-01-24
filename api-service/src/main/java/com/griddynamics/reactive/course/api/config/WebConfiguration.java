package com.griddynamics.reactive.course.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration {

    private final ExternalServiceProperties externalServiceProperties;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient orderWebClient(WebClient.Builder builder) {
        HttpClient client = HttpClient.create().responseTimeout(Duration.ofMillis(1_000));
        return builder
                .baseUrl(externalServiceProperties.getBaseUrl().getOrderSearch())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }

    @Bean
    public WebClient productWebClient(WebClient.Builder builder) {
        HttpClient client = HttpClient.create().responseTimeout(Duration.ofMillis(10_000));
        return builder
                .baseUrl(externalServiceProperties.getBaseUrl().getProductInfo())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }
}
