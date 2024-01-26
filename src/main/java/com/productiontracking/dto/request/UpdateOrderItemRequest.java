package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class UpdateOrderItemRequest {
    private Long id;
    private Long ageGroupId;
    private int quantity;
}
