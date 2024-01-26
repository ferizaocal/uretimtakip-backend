package com.productiontracking.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private Long ageGroupId;
    private String age;
    private String description;
    private int quantity;
}
