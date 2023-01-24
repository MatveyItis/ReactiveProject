package com.griddynamics.reactive.course.api.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.griddynamics.reactive.course.api.model.OrderInfo;
import com.griddynamics.reactive.course.api.persistence.User;
import com.griddynamics.reactive.course.api.repository.UserInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WireMockTest(httpPort = 8000)
@AutoConfigureWebTestClient
@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test"
)
class OrderInfoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    UserInfoRepository userInfoRepository;

    @AfterEach
    public void after() {
        WireMock.reset();
    }

    @Test
    public void getUserOrders() {
        when(userInfoRepository.findById(anyString()))
                .thenReturn(Mono.just(new User("user1", "vanya", "987654321")));

        stubOrderSearchService();
        stubProductInfoService();

        StepVerifier.create(webTestClient.get()
                        .uri(builder -> builder
                                .pathSegment("users", "{user}", "order")
                                .build("user1"))
                        .header("requestId", UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_NDJSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_NDJSON_VALUE)
                        .returnResult(OrderInfo.class)
                        .getResponseBody())
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getUserOrders_withoutProductService() {
        when(userInfoRepository.findById(anyString()))
                .thenReturn(Mono.just(new User("user1", "vanya", "987654321")));

        stubOrderSearchService();

        StepVerifier.create(webTestClient.get()
                        .uri(builder -> builder
                                .pathSegment("users", "{user}", "order")
                                .build("user1"))
                        .header("requestId", UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_NDJSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_NDJSON_VALUE)
                        .returnResult(OrderInfo.class)
                        .getResponseBody())
                .expectNextCount(4)
                .verifyComplete();
    }

    private void stubProductInfoService() {
        stubFor(get(urlPathEqualTo("/productInfoService/product/names"))
                .withQueryParam("productCode", matching("[0-9]*"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("products.json")));
    }

    private void stubOrderSearchService() {
        stubFor(get(urlPathEqualTo("/orderSearchService/order/phone"))
                .withQueryParam("phoneNumber", equalTo("987654321"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("orders.json")));
    }
}