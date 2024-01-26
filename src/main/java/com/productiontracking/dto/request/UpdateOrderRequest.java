package com.productiontracking.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdateOrderRequest {
    private Long id;
    private Long customerId;
    private Long productionCodeId;
    private Long description;
    private String status;
    private List<UpdateOrderItemRequest> orderItems;
}
