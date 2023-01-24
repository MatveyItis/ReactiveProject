package com.griddynamics.reactive.course.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "external-service")
public class ExternalServiceProperties {

    private BaseUrl baseUrl;

    @Data
    protected static class BaseUrl {

        private String orderSearch;

        private String productInfo;
    }
}
