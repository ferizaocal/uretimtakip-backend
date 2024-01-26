package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateOperationCompleteRequest;
import com.productiontracking.dto.response.ProductionTrackingResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.Operation;
import com.productiontracking.service.UserOperationService;
import com.productiontracking.utils.GetClaims;

@RestController
@RequestMapping(value = "/api/v1")
public class UserOperationController {

    @Autowired
    private UserOperationService operationService;

    @GetMapping(value = "/user-operation/completed")
    public ServiceResponse<ProductionTrackingResponse> findAllByUserIdCompleted(
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return operationService.findAllByUserIdCompleted(userId);
    }

    @GetMapping(value = "/user-operation/active")
    public ServiceResponse<ProductionTrackingResponse> findAllByUserIdActive(
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return operationService.findAllByUserIdActive(userId);
    }

    @PostMapping(value = "/user-operation/complete")
    public ServiceResponse<Operation> completeOperation(
            @RequestBody CreateOperationCompleteRequest request) {

        return operationService.setCompleted(request);
    }
}
