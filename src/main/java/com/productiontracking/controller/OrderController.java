package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateOrderRequest;
import com.productiontracking.dto.request.UpdateOrderRequest;
import com.productiontracking.dto.response.OrderResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.service.OrderService;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ServiceResponse<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/order/{id}")
    public ServiceResponse<OrderResponse> getOrderById(Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/order")
    public ServiceResponse<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.createOrder(createOrderRequest);
    }

    @PutMapping("/order")
    public ServiceResponse<OrderResponse> updateOrder(@RequestBody UpdateOrderRequest updateOrderRequest) {
        return orderService.updateOrder(updateOrderRequest);
    }

    @DeleteMapping("/order/{id}")
    public ServiceResponse<OrderResponse> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
