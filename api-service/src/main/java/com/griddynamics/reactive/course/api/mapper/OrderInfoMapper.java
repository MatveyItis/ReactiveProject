package com.griddynamics.reactive.course.api.mapper;

import com.griddynamics.reactive.course.api.dto.Order;
import com.griddynamics.reactive.course.api.dto.Product;
import com.griddynamics.reactive.course.api.model.OrderInfo;
import com.griddynamics.reactive.course.api.persistence.User;
import org.springframework.stereotype.Component;

@Component
public class OrderInfoMapper {

    public OrderInfo mapOrderInfo(Order order, User user, Product product) {
        return OrderInfo.builder()
                .phoneNumber(user.getPhone())
                .userName(user.getName())
                .orderNumber(order.getOrderNumber())
                .productCode(order.getProductCode())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }
}
