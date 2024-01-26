package com.productiontracking.service;

import com.productiontracking.dto.request.CreateOperationCompleteRequest;
import com.productiontracking.dto.response.ProductionTrackingResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.Operation;

public interface UserOperationService {
    ServiceResponse<ProductionTrackingResponse> findAllByUserIdCompleted(Long userId);

    ServiceResponse<ProductionTrackingResponse> findAllByUserIdActive(Long userId);

    ServiceResponse<Operation> setCompleted(CreateOperationCompleteRequest createOperationCompleteRequest);
}
