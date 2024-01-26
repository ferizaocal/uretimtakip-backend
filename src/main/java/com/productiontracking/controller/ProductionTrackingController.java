package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateProductionTrackingRequest;
import com.productiontracking.dto.response.ProductionTrackingResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.service.ProductionTrackingService;
import com.productiontracking.utils.GetClaims;

@RestController
@RequestMapping(value = "/api/v1")
public class ProductionTrackingController {

    @Autowired
    private ProductionTrackingService productionTrackingService;

    @PostMapping(value = "/productionTracking")
    public ServiceResponse<ProductionTrackingResponse> createProductTracking(
            @RequestBody CreateProductionTrackingRequest request,
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return productionTrackingService.createProductionTracking(request, null, userId);
    }

    @GetMapping(value = "/productionTrackings/{operationId}")
    public ServiceResponse<ProductionTrackingResponse> getProductionTrackings(
            @PathVariable Long operationId,
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return productionTrackingService.getAll(userId, operationId);
    }

    @GetMapping(value = "/productionTrackings")
    public ServiceResponse<ProductionTrackingResponse> getProductionTrackings(
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return productionTrackingService.getAll(userId);
    }
}
