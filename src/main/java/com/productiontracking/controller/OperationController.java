package com.productiontracking.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateOperationRequest;
import com.productiontracking.dto.request.UpdateOperationRequest;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.Operation;
import com.productiontracking.service.OperationService;
import com.productiontracking.utils.GetClaims;

@RestController
@RequestMapping(value = "/api/v1")
public class OperationController {

    private OperationService operationService;

    public OperationController(OperationService operationService) {
        super();
        this.operationService = operationService;
    }

    @GetMapping(value = "/operations")
    public ServiceResponse<Operation> findAll() {
        return operationService.findAll();
    }

    @GetMapping(value = "/operations/active")
    public ServiceResponse<Operation> findAllByActive() {
        return operationService.findAllByActive();
    }

    @PostMapping(value = "/operations")
    public ServiceResponse<Operation> create(@RequestBody CreateOperationRequest operation,
            @RequestHeader("Authorization") String token) {
        Long createdId = GetClaims.getUserIdFromToken(token);
        return operationService.create(operation, createdId);
    }

    @PutMapping(value = "/operations")
    public ServiceResponse<Operation> update(@RequestBody UpdateOperationRequest operation) {
        return operationService.update(operation);
    }

    @DeleteMapping(value = "/operations/{id}")
    public ServiceResponse<Operation> delete(@RequestBody Long id) {
        return operationService.delete(id);
    }

    @GetMapping(value = "/operation/next/{productionTrackingId}/{currentOperationId}")
    public ServiceResponse<Operation> getNextOperation(@PathVariable Long productionTrackingId,
            @PathVariable Long currentOperationId) {
        return operationService.nextOperation(productionTrackingId, currentOperationId);
    }
}
