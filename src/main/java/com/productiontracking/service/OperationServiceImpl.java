package com.productiontracking.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateOperationCompleteRequest;
import com.productiontracking.dto.request.CreateOperationRequest;
import com.productiontracking.dto.request.UpdateOperationRequest;
import com.productiontracking.dto.response.ProductionTrackingFabricResponse;
import com.productiontracking.dto.response.ProductionTrackingHistoryResponse;
import com.productiontracking.dto.response.ProductionTrackingResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.dto.response.UserOperationResponse;
import com.productiontracking.entity.Operation;
import com.productiontracking.entity.ProductionTracking;
import com.productiontracking.entity.UserOperation;
import com.productiontracking.exception.DuplicateOperationException;
import com.productiontracking.exception.NotFoundException;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.OperationRepository;
import com.productiontracking.repository.ProductionTrackingRepository;
import com.productiontracking.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OperationServiceImpl implements OperationService {

    @Autowired
    private ModelMapperService modelMapperService;

    private UserRepository userRepository;
    private OperationRepository operationRepository;
    private ProductionTrackingRepository productionTrackingRepository;

    public OperationServiceImpl(UserRepository userRepository, OperationRepository operationRepository,
            ProductionTrackingRepository productionTrackingRepository) {
        super();
        this.productionTrackingRepository = productionTrackingRepository;
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    public ServiceResponse<Operation> create(CreateOperationRequest createOperationRequest, Long createdBy) {
        ServiceResponse<Operation> serviceResponse = new ServiceResponse<>();
        try {
            Operation existOperation = operationRepository
                    .findByOperationNameAndIsDeleted(createOperationRequest.getOperationName(), false);
            if (existOperation != null) {
                throw new DuplicateOperationException(createOperationRequest.getOperationName());
            }
            Operation operation = modelMapperService.forRequest().map(createOperationRequest, Operation.class);
            Long productionModelId = userRepository.findById(createdBy).get().getActiveProductionModelId();
            operation.setProductionModelId(productionModelId);
            operationRepository.save(operation);
            serviceResponse.setEntity(operation).setIsSuccessful(true);

        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Operation> update(UpdateOperationRequest updateOperationRequest) {
        ServiceResponse<Operation> serviceResponse = new ServiceResponse<>();
        try {
            Operation existOperationByName = operationRepository
                    .findByOperationNameAndIsDeleted(updateOperationRequest.getOperationName(), false);
            if (existOperationByName != null && !existOperationByName.getId().equals(updateOperationRequest.getId())) {
                throw new DuplicateOperationException(updateOperationRequest.getOperationName());
            }
            Operation exitOperation = operationRepository.findById(updateOperationRequest.getId()).get();
            if (exitOperation == null) {
                throw new NotFoundException("Operation not found id: " + updateOperationRequest.getId());
            }
            exitOperation.setOperationName(updateOperationRequest.getOperationName());
            exitOperation.setOperationNumber(updateOperationRequest.getOperationNumber());
            exitOperation.setStatus(updateOperationRequest.getStatus());
            operationRepository.save(exitOperation);
            serviceResponse.setEntity(exitOperation).setIsSuccessful(true);
        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Operation> delete(Long id) {
        ServiceResponse<Operation> serviceResponse = new ServiceResponse<>();
        try {

        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Operation> updateStatusById(Long id) {
        ServiceResponse<Operation> serviceResponse = new ServiceResponse<>();
        try {
            Operation existOperation = operationRepository.findById(id).get();
            if (existOperation == null) {
                throw new NotFoundException("Operation not found id: " + id);
            }
            existOperation.setStatus(existOperation.getStatus().equals(Operation.Status.ACTIVE.toString())
                    ? Operation.Status.INACTIVE.toString()
                    : Operation.Status.ACTIVE.toString());
            operationRepository.save(existOperation);
            serviceResponse.setEntity(existOperation).setIsSuccessful(true);
        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Operation> findAll() {
        ServiceResponse<Operation> serviceResponse = new ServiceResponse<>();
        try {
            List<Operation> operations = operationRepository.findAll();
            serviceResponse.setList(operations).setIsSuccessful(true);
        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Operation> findAllByActive() {
        ServiceResponse<Operation> serviceResponse = new ServiceResponse<>();
        try {
            List<Operation> operations = operationRepository.findAllByStatus(Operation.Status.ACTIVE.toString());
            serviceResponse.setList(operations).setIsSuccessful(true);
        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Operation> nextOperation(Long productTrackingId, Long currentOperationId) {
        ServiceResponse<Operation> serviceResponse = new ServiceResponse<>();
        try {
            ProductionTracking productionTracking = productionTrackingRepository.findById(productTrackingId).get();
            if (productionTracking == null) {
                throw new NotFoundException("Production Tracking not found id: " + productTrackingId);
            }
            Operation currentOperation = operationRepository.findById(currentOperationId).get();
            if (currentOperation == null) {
                throw new NotFoundException("Operation not found id: " + currentOperationId);
            }
            List<Operation> operations = operationRepository.findAllByIsDeletedAndProductionModelId(false,
                    productionTracking.getProductionModelId());

            Operation nextOperation = operations.stream()
                    .filter(x -> x.getOperationNumber() == currentOperation.getOperationNumber() + 1).findFirst()
                    .get();

            serviceResponse.setEntity(nextOperation).setIsSuccessful(true);
        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

}
