package com.griddynamics.reactive.course.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String productId;
    private String productCode;
    private String productName;
    private double score;
}
