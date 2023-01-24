package com.griddynamics.reactive.course.api.service;

import com.griddynamics.reactive.course.api.dto.Product;
import com.griddynamics.reactive.course.api.mapper.OrderInfoMapper;
import com.griddynamics.reactive.course.api.model.OrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderInfoService {

    private final ApiService apiService;
    private final UserService userService;
    private final OrderInfoMapper orderInfoMapper;

    public Flux<OrderInfo> getUserOrders(String userId, String requestId) {
        return userService.getUserInfoById(userId)
                .flatMapMany(user -> apiService.getOrdersByPhoneNumber(user.getPhone(), requestId)
                        .flatMap(order -> apiService.getProductsByProductCode(order.getProductCode())
                                .map(list -> list.stream().max(Comparator.comparingDouble(Product::getScore)))
                                .map(product -> orderInfoMapper.mapOrderInfo(order, user, product.orElseGet(Product::new)))));
    }
}
