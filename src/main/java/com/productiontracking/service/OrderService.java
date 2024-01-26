package com.productiontracking.service;

import com.productiontracking.dto.request.CreateOrderRequest;
import com.productiontracking.dto.request.UpdateOrderRequest;
import com.productiontracking.dto.response.OrderResponse;
import com.productiontracking.dto.response.ServiceResponse;

public interface OrderService {
    ServiceResponse<OrderResponse> getAllOrders();

    ServiceResponse<OrderResponse> getOrderById(Long id);

    ServiceResponse<OrderResponse> createOrder(CreateOrderRequest order);

    ServiceResponse<OrderResponse> updateOrder(UpdateOrderRequest order);

    ServiceResponse<OrderResponse> deleteOrder(Long id);
}
