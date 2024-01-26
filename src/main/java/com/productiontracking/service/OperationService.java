package com.productiontracking.service;

import com.productiontracking.dto.request.CreateOperationCompleteRequest;
import com.productiontracking.dto.request.CreateOperationRequest;
import com.productiontracking.dto.request.UpdateOperationRequest;
import com.productiontracking.dto.response.ProductionTrackingResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.Operation;

public interface OperationService {
    ServiceResponse<Operation> create(CreateOperationRequest createOperationRequest, Long createdBy);

    ServiceResponse<Operation> update(UpdateOperationRequest createOperationRequest);

    ServiceResponse<Operation> delete(Long id);

    ServiceResponse<Operation> updateStatusById(Long id);

    ServiceResponse<Operation> findAll();

    ServiceResponse<Operation> findAllByActive();

    ServiceResponse<Operation> nextOperation(Long productTrackingId, Long currentOperationId);
}
