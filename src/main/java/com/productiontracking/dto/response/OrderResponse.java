package com.productiontracking.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String customerFirstName;
    private String customerLastName;
    private String productionCode;
    private String status;
    private List<OrderItemResponse> orderItems;
}
