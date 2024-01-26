package com.productiontracking.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateOrderRequest {
    Long customerId;
    Long productionCodeId;
    List<CreateOrderItemRequest> orderItems;
}
