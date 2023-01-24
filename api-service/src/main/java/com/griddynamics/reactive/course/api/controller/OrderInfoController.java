package com.griddynamics.reactive.course.api.controller;

import com.griddynamics.reactive.course.api.model.OrderInfo;
import com.griddynamics.reactive.course.api.service.OrderInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import static com.griddynamics.reactive.course.api.util.LogUtils.logOnError;
import static com.griddynamics.reactive.course.api.util.LogUtils.logOnNext;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderInfoController {

    private final OrderInfoService orderInfoService;

    @GetMapping(value = "/users/{userId}/order", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<OrderInfo> getUserOrders(@PathVariable String userId,
                                         @RequestHeader String requestId) {
        log.info("getUserOrders with userId={}", userId);
        return orderInfoService.getUserOrders(userId, requestId)
                .doOnEach(logOnNext(order -> log.info("Found order {} for user {}", order, userId)))
                .doOnEach(logOnError(err -> log.error("Error while getting orders for user {}", userId, err)))
                .contextWrite(Context.of("CONTEXT_KEY", requestId));
    }
}
