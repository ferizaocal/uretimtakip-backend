package com.productiontracking.service;

import org.springframework.web.multipart.MultipartFile;

import com.productiontracking.dto.request.CreateProductionTrackingRequest;
import com.productiontracking.dto.response.ProductionTrackingResponse;
import com.productiontracking.dto.response.ServiceResponse;

public interface ProductionTrackingService {
    ServiceResponse<ProductionTrackingResponse> createProductionTracking(CreateProductionTrackingRequest request,
            MultipartFile file, Long userId);

    ServiceResponse<ProductionTrackingResponse> delete(Long id);

    ServiceResponse<ProductionTrackingResponse> getAll(Long userId, Long operationId);

    ServiceResponse<ProductionTrackingResponse> getAll(Long userId);
}
