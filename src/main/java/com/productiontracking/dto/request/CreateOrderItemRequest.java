package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class CreateOrderItemRequest {
    private Long ageGroupId;
    private int quantity;
    private String description;
}
