package com.productiontracking.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateOperationCompleteRequest;
import com.productiontracking.dto.response.ProductionTrackingFabricResponse;
import com.productiontracking.dto.response.ProductionTrackingHistoryResponse;
import com.productiontracking.dto.response.ProductionTrackingResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.dto.response.UserOperationResponse;
import com.productiontracking.entity.Operation;
import com.productiontracking.entity.ProductionCodeHistory;
import com.productiontracking.entity.ProductionTracking;
import com.productiontracking.entity.ProductionTrackingHistory;
import com.productiontracking.entity.UserOperation;
import com.productiontracking.exception.NotFoundException;
import com.productiontracking.repository.OperationRepository;
import com.productiontracking.repository.ProductionCodeHistoryRepository;
import com.productiontracking.repository.ProductionCodeRepository;
import com.productiontracking.repository.ProductionTrackingHistoryRepository;
import com.productiontracking.repository.ProductionTrackingRepository;
import com.productiontracking.repository.UserOperationRepository;

@Service
@Transactional
public class UserOperationServiceImpl implements UserOperationService {

    @Autowired
    private ProductionTrackingRepository productionTrackingRepository;
    @Autowired
    private UserOperationRepository userOperationRepository;
    @Autowired
    private ProductionTrackingHistoryRepository productionTrackingHistoryRepository;
    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ProductionCodeHistoryRepository productionCodeHistoryRepository;

    public Boolean isOperationExistFromCompleted(ProductionTracking productionTracking, Long userId) {
        Boolean isDeleted = productionTracking.getIsDeleted();
        UserOperation lastItemOperation = productionTracking.getUserOperations().stream()
                .filter(x -> x.getIsCompleted() == true && x.getUserId().equals(userId)).findFirst().get();
        Boolean isCompletedAndEqualUserId = lastItemOperation.getIsCompleted() == true
                && lastItemOperation.getIsDeleted() == false
                && lastItemOperation.getUserId().equals(userId);
        return !isDeleted && isCompletedAndEqualUserId;
    }

    @Override
    public ServiceResponse<ProductionTrackingResponse> findAllByUserIdCompleted(Long userId) {
        ServiceResponse<ProductionTrackingResponse> serviceResponse = new ServiceResponse<>();
        try {
            List<UserOperation> userOperations = userOperationRepository.findAllByUserId(userId);
            List<ProductionTracking> productionTrackings = userOperations.stream()
                    .map(x -> x.getProductionTracking()).collect(Collectors.toList());

            List<ProductionTrackingResponse> productionTrackingResponses = productionTrackings.stream()
                    .filter(x -> isOperationExistFromCompleted(x, userId)).map(x -> {
                        ProductionTrackingResponse productionTrackingResponse = new ProductionTrackingResponse();
                        productionTrackingResponse.setId(x.getId());
                        productionTrackingResponse.setOperationId(x.getOperationId());
                        productionTrackingResponse.setPartyNumber(x.getPartyNumber());
                        productionTrackingResponse.setDescription(x.getDescription());
                        productionTrackingResponse.setCreatedDate(x.getCreatedDate());
                        int totalQuantity = x.getProductionTrackingFabrics().stream()
                                .mapToInt(f -> f.getQuantity()).sum();
                        double totalMetre = x.getProductionTrackingFabrics().stream()
                                .mapToDouble(f -> f.getMetre()).sum();
                        productionTrackingResponse.setTotalQuantity(totalQuantity);
                        productionTrackingResponse.setTotalMetre(totalMetre);
                        productionTrackingResponse.setAgeGroup(x.getAgeGroup());
                        productionTrackingResponse.setProductionModel(x.getProductionModel());
                        productionTrackingResponse.setProductionCode(x.getProductionCode());
                        productionTrackingResponse.setOperation(x.getOperation());
                        List<UserOperationResponse> operations = x.getUserOperations().stream().map(uo -> {
                            UserOperationResponse userOperationResponse = new UserOperationResponse();
                            userOperationResponse.setId(uo.getId());
                            userOperationResponse.setUserId(uo.getUserId());
                            userOperationResponse.setFirstName(uo.getUser().getFirstName());
                            userOperationResponse.setLastName(uo.getUser().getLastName());
                            userOperationResponse.setOperation(uo.getOperation());
                            userOperationResponse.setIsCompleted(uo.getIsCompleted());
                            userOperationResponse.setCreatedDate(uo.getCreatedDate());
                            return userOperationResponse;
                        }).collect(Collectors.toList());
                        UserOperationResponse currentUserOperation = operations.stream()
                                .filter(currentOperation -> currentOperation.getUserId().equals(userId)).findFirst()
                                .get();
                        productionTrackingResponse.setUserOperation(currentUserOperation);
                        productionTrackingResponse.setUserOperations(operations);
                        List<ProductionTrackingFabricResponse> productionTrackingFabricResponses = x
                                .getProductionTrackingFabrics().stream().map(y -> {
                                    ProductionTrackingFabricResponse productionTrackingFabricResponse = new ProductionTrackingFabricResponse();
                                    productionTrackingFabricResponse.setId(y.getId());
                                    productionTrackingFabricResponse.setFabricId(y.getFabricId());
                                    productionTrackingFabricResponse.setQuantity(y.getQuantity());
                                    productionTrackingFabricResponse.setMetre(y.getMetre());
                                    productionTrackingFabricResponse
                                            .setFabricName(y.getFabric().getFabricModel().getFabricBrand().getName());
                                    productionTrackingFabricResponse
                                            .setFabricModel(y.getFabric().getFabricModel().getName());

                                    return productionTrackingFabricResponse;
                                }).collect(Collectors.toList());
                        productionTrackingResponse.setProductionTrackingFabrics(productionTrackingFabricResponses);

                        List<ProductionTrackingHistoryResponse> productionTrackingHistories = x
                                .getProductionTrackingHistories()
                                .stream().map(c -> {
                                    ProductionTrackingHistoryResponse productionTrackingHistoryResponse = new ProductionTrackingHistoryResponse();
                                    productionTrackingHistoryResponse.setId(c.getId());
                                    productionTrackingHistoryResponse
                                            .setProductionTrackingId(c.getProductionTrackingId());
                                    productionTrackingHistoryResponse.setOperationId(c.getOperationId());
                                    productionTrackingHistoryResponse
                                            .setOperationName(c.getOperation().getOperationName());
                                    productionTrackingHistoryResponse
                                            .setOperationNumber(c.getOperation().getOperationNumber());
                                    productionTrackingHistoryResponse.setCompletionDate(c.getCompletionDate());
                                    productionTrackingHistoryResponse.setCreatedDate(c.getCreatedDate());
                                    return productionTrackingHistoryResponse;

                                }).collect(Collectors.toList());
                        productionTrackingResponse.setProductionTrackingHistories(productionTrackingHistories);
                        return productionTrackingResponse;
                    }).collect(Collectors.toList());

            serviceResponse.setList(productionTrackingResponses).setIsSuccessful(true);

        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

    public Boolean isOperationExistFromActive(ProductionTracking productionTracking, Long userId) {
        Boolean isDeleted = productionTracking.getIsDeleted();
        // last item
        UserOperation lastItemOperation = productionTracking.getUserOperations()
                .get(productionTracking.getUserOperations().size() - 1);
        Boolean isCompletedAndEqualUserId = lastItemOperation.getIsCompleted() == false
                && lastItemOperation.getIsDeleted() == false && lastItemOperation.getUserId().equals(userId);
        return !isDeleted && isCompletedAndEqualUserId;
    }

    @Override
    public ServiceResponse<ProductionTrackingResponse> findAllByUserIdActive(Long userId) {
        ServiceResponse<ProductionTrackingResponse> serviceResponse = new ServiceResponse<>();
        try {
            List<ProductionTracking> productionTrackings = productionTrackingRepository
                    .findAll().stream()
                    .filter(x -> isOperationExistFromActive(x, userId)).collect(Collectors.toList());

            List<ProductionTrackingResponse> productionTrackingResponses = productionTrackings.stream().map(x -> {
                ProductionTrackingResponse productionTrackingResponse = new ProductionTrackingResponse();
                productionTrackingResponse.setId(x.getId());
                productionTrackingResponse.setOperationId(x.getOperationId());
                productionTrackingResponse.setPartyNumber(x.getPartyNumber());
                productionTrackingResponse.setDescription(x.getDescription());
                productionTrackingResponse.setCreatedDate(x.getCreatedDate());
                int totalQuantity = x.getProductionTrackingFabrics().stream()
                        .mapToInt(f -> f.getQuantity()).sum();
                double totalMetre = x.getProductionTrackingFabrics().stream()
                        .mapToDouble(f -> f.getMetre()).sum();
                productionTrackingResponse.setTotalQuantity(totalQuantity);
                productionTrackingResponse.setTotalMetre(totalMetre);
                productionTrackingResponse.setAgeGroup(x.getAgeGroup());
                productionTrackingResponse.setProductionModel(x.getProductionModel());
                productionTrackingResponse.setProductionCode(x.getProductionCode());
                productionTrackingResponse.setOperation(x.getOperation());
                List<UserOperationResponse> operations = x.getUserOperations().stream().map(uo -> {
                    UserOperationResponse userOperationResponse = new UserOperationResponse();
                    userOperationResponse.setId(uo.getId());
                    userOperationResponse.setUserId(uo.getUserId());
                    userOperationResponse.setFirstName(uo.getUser().getFirstName());
                    userOperationResponse.setLastName(uo.getUser().getLastName());
                    userOperationResponse.setOperation(uo.getOperation());
                    userOperationResponse.setIsCompleted(uo.getIsCompleted());
                    userOperationResponse.setCreatedDate(uo.getCreatedDate());
                    return userOperationResponse;
                }).collect(Collectors.toList());
                UserOperationResponse currentUserOperation = operations.stream()
                        .filter(currentOperation -> currentOperation.getOperation().getId().equals(x.getOperationId()))
                        .findFirst().get();
                productionTrackingResponse.setUserOperation(currentUserOperation);
                productionTrackingResponse.setUserOperations(operations);
                List<ProductionTrackingFabricResponse> productionTrackingFabricResponses = x
                        .getProductionTrackingFabrics().stream().map(y -> {
                            ProductionTrackingFabricResponse productionTrackingFabricResponse = new ProductionTrackingFabricResponse();
                            productionTrackingFabricResponse.setId(y.getId());
                            productionTrackingFabricResponse.setFabricId(y.getFabricId());
                            productionTrackingFabricResponse.setQuantity(y.getQuantity());
                            productionTrackingFabricResponse.setMetre(y.getMetre());
                            productionTrackingFabricResponse
                                    .setFabricName(y.getFabric().getFabricModel().getFabricBrand().getName());
                            productionTrackingFabricResponse.setFabricModel(y.getFabric().getFabricModel().getName());

                            return productionTrackingFabricResponse;
                        }).collect(Collectors.toList());
                productionTrackingResponse.setProductionTrackingFabrics(productionTrackingFabricResponses);

                List<ProductionTrackingHistoryResponse> productionTrackingHistories = x.getProductionTrackingHistories()
                        .stream().map(c -> {
                            ProductionTrackingHistoryResponse productionTrackingHistoryResponse = new ProductionTrackingHistoryResponse();
                            productionTrackingHistoryResponse.setId(c.getId());
                            productionTrackingHistoryResponse.setProductionTrackingId(c.getProductionTrackingId());
                            productionTrackingHistoryResponse.setOperationId(c.getOperationId());
                            productionTrackingHistoryResponse.setOperationName(c.getOperation().getOperationName());
                            productionTrackingHistoryResponse.setOperationNumber(c.getOperation().getOperationNumber());
                            productionTrackingHistoryResponse.setCompletionDate(c.getCompletionDate());
                            productionTrackingHistoryResponse.setCreatedDate(c.getCreatedDate());
                            return productionTrackingHistoryResponse;

                        }).collect(Collectors.toList());
                productionTrackingResponse.setProductionTrackingHistories(productionTrackingHistories);
                return productionTrackingResponse;
            }).collect(Collectors.toList());

            serviceResponse.setList(productionTrackingResponses).setIsSuccessful(true);
        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

    @Override
    public ServiceResponse<Operation> setCompleted(CreateOperationCompleteRequest request) {
        ServiceResponse<Operation> serviceResponse = new ServiceResponse<>();
        try {
            Optional<ProductionTracking> productionTracking = productionTrackingRepository
                    .findById(request.getProductTrackingId());
            if (!productionTracking.isPresent()) {
                throw new NotFoundException("Production Tracking not found id: " + request.getProductTrackingId());
            }
            Optional<UserOperation> userOperation = userOperationRepository.findById(request.getUserOperationId());
            if (!userOperation.isPresent()) {
                throw new NotFoundException("User Operation not found id: " + request.getUserOperationId());
            }
            userOperation.get().setIsCompleted(true);
            userOperationRepository.save(userOperation.get());
            Date completedDate = new Date();
            ProductionTrackingHistory productionTrackingHistory = productionTrackingHistoryRepository
                    .findByProductionTrackingIdAndOperationId(request.getProductTrackingId(),
                            userOperation.get().getOperationId());
            productionTrackingHistory.setCompletionDate(completedDate);
            productionTrackingHistoryRepository.save(productionTrackingHistory);

            List<Operation> operations = operationRepository.findAllByIsDeletedAndProductionModelId(false,
                    productionTracking.get().getProductionModelId());
            Operation nextOperation = operations.stream()
                    .filter(x -> x.getOperationNumber() == userOperation.get().getOperation().getOperationNumber() + 1)
                    .findFirst().get();
            productionTracking.get().setOperationId(nextOperation.getId());
            productionTrackingRepository.save(productionTracking.get());

            ProductionTrackingHistory newProductionTrackingHistory = new ProductionTrackingHistory();
            newProductionTrackingHistory.setProductionTrackingId(request.getProductTrackingId());
            newProductionTrackingHistory.setOperationId(nextOperation.getId());
            newProductionTrackingHistory.setCompletionDate(null);
            productionTrackingHistoryRepository.save(newProductionTrackingHistory);

            boolean isLastOperation = operations.stream()
                    .filter(x -> x.getOperationNumber() == userOperation.get().getOperation().getOperationNumber() + 1)
                    .findFirst().isPresent();

            if ((nextOperation.getOperationName().contains("Mağaza")
                    || nextOperation.getOperationName().contains("Store")) && isLastOperation) {
                if (productionTracking.get().getProductionTrackingFabrics() != null
                        && productionTracking.get().getProductionTrackingFabrics().size() > 0) {
                    productionTracking.get().getProductionTrackingFabrics().forEach(fabric -> {
                        ProductionCodeHistory productionCodeHistory = new ProductionCodeHistory();
                        productionCodeHistory.setProductionCodeId(productionTracking.get().getProductionCodeId());
                        productionCodeHistory.setProductionId(productionTracking.get().getId());
                        productionCodeHistory.setType(ProductionCodeHistory.Type.IN.toString());
                        productionCodeHistory.setQuantity(fabric.getQuantity());
                        productionCodeHistoryRepository.save(productionCodeHistory);
                    });
                }
            } else {
                UserOperation newUserOperation = new UserOperation();
                newUserOperation.setUserId(request.getTargetUserId());
                newUserOperation.setOperationId(nextOperation.getId());
                newUserOperation.setProductionTrackingId(request.getProductTrackingId());
                newUserOperation.setIsCompleted(false);
                userOperationRepository.save(newUserOperation);
            }

            serviceResponse.setEntity(nextOperation).setIsSuccessful(true);
        } catch (Exception e) {
            serviceResponse.setExceptionMessage(e.getMessage().toString())
                    .setHasExceptionError(true);
        }
        return serviceResponse;
    }

}
