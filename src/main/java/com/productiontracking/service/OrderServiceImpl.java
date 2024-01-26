package com.productiontracking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateOrderItemRequest;
import com.productiontracking.dto.request.CreateOrderRequest;
import com.productiontracking.dto.request.UpdateOrderItemRequest;
import com.productiontracking.dto.request.UpdateOrderRequest;
import com.productiontracking.dto.response.OrderResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.AgeGroup;
import com.productiontracking.entity.Order;
import com.productiontracking.entity.OrderItem;
import com.productiontracking.entity.ProductionCode;
import com.productiontracking.entity.User;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.AgeGroupRepository;
import com.productiontracking.repository.OrderItemRepository;
import com.productiontracking.repository.OrderRepository;
import com.productiontracking.repository.ProductionCodeRepository;
import com.productiontracking.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ModelMapperService modelMapperService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductionCodeRepository productionCodeRepository;

    @Autowired
    private AgeGroupRepository ageGroupRepository;

    @Override
    public ServiceResponse<OrderResponse> getAllOrders() {
        ServiceResponse<OrderResponse> response = new ServiceResponse<>();
        try {
            List<OrderResponse> orderResponseList = new ArrayList<>();
            List<Order> orders = orderRepository.findAll().stream().filter(x -> x.getIsDeleted() == false)
                    .collect(Collectors.toList());
            for (Order order : orders) {
                OrderResponse orderResponse = modelMapperService.forResponse().map(order, OrderResponse.class);

                orderResponseList.add(orderResponse);
            }
            response.setList(orderResponseList).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

    @Override
    public ServiceResponse<OrderResponse> getOrderById(Long id) {
        ServiceResponse<OrderResponse> response = new ServiceResponse<>();
        try {
            Order order = orderRepository.findById(id).orElse(null);
            OrderResponse orderResponse = modelMapperService.forResponse().map(order, OrderResponse.class);
            response.setEntity(orderResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());

        }
        return response;
    }

    @Override
    public ServiceResponse<OrderResponse> createOrder(CreateOrderRequest order) {
        ServiceResponse<OrderResponse> response = new ServiceResponse<>();
        try {
            User customer = userRepository.findById(order.getCustomerId()).orElse(null);
            ProductionCode productionCode = productionCodeRepository.findById(order.getProductionCodeId()).orElse(null);
            Order newOrder = new Order();
            newOrder.setProductionCode(productionCode);
            newOrder.setCustomer(customer);
            newOrder.setStatus(Order.Status.WAITING.toString());
            newOrder = orderRepository.save(newOrder);
            log.info("Order created with id: {}", newOrder.getId());
            for (CreateOrderItemRequest orderItem : order.getOrderItems()) {
                OrderItem newOrderItem = modelMapperService.forRequest().map(orderItem, OrderItem.class);
                AgeGroup ageGroup = ageGroupRepository.findById(orderItem.getAgeGroupId()).orElse(null);
                newOrderItem.setAgeGroup(ageGroup);
                newOrderItem.setOrder(newOrder);
                orderItemRepository.save(newOrderItem);
                log.info("Order item created with id: {}", newOrderItem.getId());
            }
            response.setIsSuccessful(true);
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

    @Override
    public ServiceResponse<OrderResponse> updateOrder(UpdateOrderRequest order) {
        ServiceResponse<OrderResponse> response = new ServiceResponse<>();
        try {
            Order existingOrder = orderRepository.findById(order.getId()).orElse(null);
            if (existingOrder != null) {
                existingOrder.setStatus(order.getStatus());
                existingOrder = orderRepository.save(existingOrder);
                log.info("Order updated with id: {}", existingOrder.getId());
                for (UpdateOrderItemRequest orderItem : order.getOrderItems()) {
                    OrderItem existingOrderItem = orderItemRepository.findById(orderItem.getId()).orElse(null);
                    if (existingOrderItem != null) {
                        existingOrderItem.setQuantity(orderItem.getQuantity());
                        existingOrderItem = orderItemRepository.save(existingOrderItem);
                        log.info("Order item updated with id: {}", existingOrderItem.getId());
                    } else {
                        // new order item
                        OrderItem newOrderItem = modelMapperService.forRequest().map(orderItem, OrderItem.class);
                        newOrderItem.setOrder(existingOrder);
                        orderItemRepository.save(newOrderItem);
                        log.info("Order item created with id: {}", newOrderItem.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error updating order: {}", e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<OrderResponse> deleteOrder(Long id) {
        ServiceResponse<OrderResponse> response = new ServiceResponse<>();
        try {
            Order existingOrder = orderRepository.findById(id).orElse(null);
            if (existingOrder != null) {
                existingOrder.setIsDeleted(true);

                if (existingOrder.getOrderItems() != null && existingOrder.getOrderItems().size() > 0)
                    orderItemRepository.saveAll(existingOrder.getOrderItems().stream().map(x -> {
                        x.setIsDeleted(true);
                        return x;
                    }).collect(Collectors.toList()));

                orderRepository.save(existingOrder);
                log.info("Order deleted with id: {}", existingOrder.getId());
            }
        } catch (Exception e) {
            log.error("Error deleting order: {}", e.getMessage());
        }
        return response;
    }

}
