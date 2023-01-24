package com.griddynamics.reactive.course.api.service;

import com.griddynamics.reactive.course.api.dto.Order;
import com.griddynamics.reactive.course.api.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.griddynamics.reactive.course.api.util.LogUtils.logOnError;
import static com.griddynamics.reactive.course.api.util.LogUtils.logOnNext;

@Slf4j
@Service
public class ApiService {

    private final WebClient orderWebClient;
    private final WebClient productWebClient;

    public ApiService(@Qualifier("orderWebClient") WebClient orderWebClient,
                      @Qualifier("productWebClient") WebClient productWebClient) {
        this.orderWebClient = orderWebClient;
        this.productWebClient = productWebClient;
    }

    public Flux<Order> getOrdersByPhoneNumber(String phoneNumber, String requestId) {
        return orderWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/order/phone")
                        .queryParam("phoneNumber", phoneNumber)
                        .build())
                .retrieve()
                .bodyToFlux(Order.class)
                .doOnEach(logOnNext(resp -> log.info("Received response from order-info-service = {}", resp)))
                .doOnEach(logOnError(err -> log.error("Got error while trying to receive orders", err)));
    }

    public Mono<List<Product>> getProductsByProductCode(String productCode) {
        return productWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/product/names")
                        .queryParam("productCode", productCode)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Product>>() {
                })
                .onErrorResume(e -> Mono.just(List.of()))
                .doOnEach(logOnNext(resp -> log.info("Received response from product-search-service = {}", resp)))
                .doOnEach(logOnError(err -> log.error("Got error while trying to receive products", err)));
    }
}
