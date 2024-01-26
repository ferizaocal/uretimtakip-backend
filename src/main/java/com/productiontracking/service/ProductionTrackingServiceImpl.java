package com.productiontracking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.productiontracking.dto.request.CreateProductionFabricRequest;
import com.productiontracking.dto.request.CreateProductionTrackingRequest;
import com.productiontracking.dto.response.ProductionTrackingFabricResponse;
import com.productiontracking.dto.response.ProductionTrackingHistoryResponse;
import com.productiontracking.dto.response.ProductionTrackingResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.dto.response.UserOperationResponse;
import com.productiontracking.entity.FabricHistory;
import com.productiontracking.entity.Operation;
import com.productiontracking.entity.ProductionTracking;
import com.productiontracking.entity.ProductionTrackingFabric;
import com.productiontracking.entity.ProductionTrackingHistory;
import com.productiontracking.entity.User;
import com.productiontracking.entity.UserOperation;
import com.productiontracking.exception.NotFoundException;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.FabricHistoryRepository;
import com.productiontracking.repository.OperationRepository;
import com.productiontracking.repository.ProductionTrackingFabricRepository;
import com.productiontracking.repository.ProductionTrackingHistoryRepository;
import com.productiontracking.repository.ProductionTrackingRepository;
import com.productiontracking.repository.UserOperationRepository;
import com.productiontracking.repository.UserRepository;
import com.productiontracking.utils.FileUploadUtils;

@Transactional
@Service
public class ProductionTrackingServiceImpl implements ProductionTrackingService {

    @Autowired
    ModelMapperService modelMapperService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    OperationRepository operationRepository;

    @Autowired
    ProductionTrackingRepository productionTrackingRepository;

    @Autowired
    FabricHistoryRepository fabricHistoryRepository;
    @Autowired
    UserOperationRepository userOperationRepository;
    @Autowired
    ProductionTrackingFabricRepository productionTrackingFabricRepository;
    @Autowired
    ProductionTrackingHistoryRepository productionTrackingHistoryRepository;

    @Override
    public ServiceResponse<ProductionTrackingResponse> createProductionTracking(
            CreateProductionTrackingRequest request, MultipartFile file, Long userId) {
        ServiceResponse<ProductionTrackingResponse> response = new ServiceResponse<>();
        try {
            if (request.getFabrics().size() == 0) {
                throw new Exception("Fabric list is empty");
            }
            ProductionTracking isExitsPartNumber = productionTrackingRepository
                    .findByPartyNumberAndIsDeletedAndProductionModelId(request.getPartyNumber(), false,
                            userRepository.findById(userId).get().getActiveProductionModelId());
            if (isExitsPartNumber != null) {
                throw new Exception("Part number already exists");
            }

            Optional<User> admin = userRepository.findById(userId);
            if (!admin.isPresent()) {
                throw new NotFoundException("User not found");
            }
            if (admin.get().getActiveProductionModelId() == null) {
                throw new NotFoundException("Production model not found");
            }
            Operation operation = operationRepository.findByOperationNumberAndIsDeletedAndProductionModelId(1, false,
                    admin.get().getActiveProductionModelId());
            if (operation == null) {
                throw new NotFoundException("Operation not found");
            }
            ProductionTracking _vTracking = new ProductionTracking();
            if (file != null) {
                String _vFileName = UUID.randomUUID().toString() + file.getOriginalFilename();
                _vTracking.setFileName(_vFileName);
                _vTracking.setImage(FileUploadUtils.compressFile(file.getBytes()));
            }
            _vTracking.setProductionModelId(admin.get().getActiveProductionModelId());
            _vTracking.setProductionCodeId(request.getProductionCodeId());
            _vTracking.setAgeGroupId(request.getAgeGroupId());
            _vTracking.setOperationId(operation.getId());
            _vTracking.setDescription(request.getDescription());
            _vTracking.setPartyNumber(request.getPartyNumber());
            productionTrackingRepository.save(_vTracking);
            for (CreateProductionFabricRequest fabric : request.getFabrics()) {
                ProductionTrackingFabric trackingFabric = new ProductionTrackingFabric();
                trackingFabric.setProductionTrackingId(_vTracking.getId());
                trackingFabric.setFabricId(fabric.getFabricId());
                trackingFabric.setQuantity(fabric.getQuantity());
                trackingFabric.setMetre(fabric.getMetre());
                productionTrackingFabricRepository.save(trackingFabric);
                FabricHistory fabricHistory = new FabricHistory();
                fabricHistory.setFabricId(fabric.getFabricId());
                fabricHistory.setType(FabricHistory.Type.OUT.toString());
                fabricHistory.setQuantity(fabric.getMetre());
                fabricHistoryRepository.save(fabricHistory);
            }
            UserOperation userOperation = new UserOperation();
            userOperation.setUserId(request.getUserId());
            userOperation.setProductionTrackingId(_vTracking.getId());
            userOperation.setIsCompleted(false);
            userOperation.setOperationId(operation.getId());
            userOperationRepository.save(userOperation);

            ProductionTrackingHistory trackingHistory = new ProductionTrackingHistory();
            trackingHistory.setProductionTrackingId(_vTracking.getId());
            trackingHistory.setOperationId(operation.getId());
            productionTrackingHistoryRepository.save(trackingHistory);

            response.setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<ProductionTrackingResponse> delete(Long id) {
        ServiceResponse<ProductionTrackingResponse> response = new ServiceResponse<>();
        try {

        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<ProductionTrackingResponse> getAll(Long userId, Long operationId) {
        ServiceResponse<ProductionTrackingResponse> response = new ServiceResponse<>();
        try {

            User admin = userRepository.findById(userId).get();
            if (admin.getActiveProductionModelId() == null) {
                throw new NotFoundException("Production model not found");
            }
            Operation operation = operationRepository.findById(operationId).get();
            if (operation == null) {
                throw new NotFoundException("Operation not found");
            }
            List<ProductionTracking> productionTrackings = productionTrackingRepository
                    .findByProductionModelIdAndOperationIdAndIsDeleted(admin.getActiveProductionModelId(), operationId,
                            false);
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
                Optional<UserOperationResponse> currentUserOperation = operations.stream()
                        .filter(currentOperation -> currentOperation.getOperation().getId().equals(operationId))
                        .findFirst();
                if (currentUserOperation.isPresent()) {
                    productionTrackingResponse.setUserOperation(currentUserOperation.get());
                } else {
                    UserOperationResponse userOperationResponse = new UserOperationResponse();
                    userOperationResponse.setOperation(operation);
                    userOperationResponse.setIsCompleted(false);
                    userOperationResponse.setCreatedDate(x.getProductionTrackingHistories()
                            .get(x.getProductionTrackingHistories().size() - 1).getCreatedDate());
                    operations.add(userOperationResponse);
                    productionTrackingResponse.setUserOperation(userOperationResponse);
                }

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

            response.setList(productionTrackingResponses).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<ProductionTrackingResponse> getAll(Long userId) {

        ServiceResponse<ProductionTrackingResponse> response = new ServiceResponse<>();
        try {
            User admin = userRepository.findById(userId).get();
            if (admin.getActiveProductionModelId() == null) {
                throw new NotFoundException("Production model not found");
            }
            List<ProductionTracking> productionTrackings = productionTrackingRepository
                    .findByProductionModelIdAndIsDeleted(admin.getActiveProductionModelId(), false);

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
                Optional<UserOperationResponse> currentUserOperation = operations.stream()
                        .filter(currentOperation -> currentOperation.getOperation().getId().equals(x.getOperationId()))
                        .findFirst();
                if (currentUserOperation.isPresent()) {
                    productionTrackingResponse.setUserOperation(currentUserOperation.get());
                } else {
                    UserOperationResponse userOperationResponse = new UserOperationResponse();
                    userOperationResponse.setOperation(x.getOperation());
                    userOperationResponse.setIsCompleted(false);
                    userOperationResponse.setCreatedDate(x.getProductionTrackingHistories()
                            .get(x.getProductionTrackingHistories().size() - 1).getCreatedDate());
                    operations.add(userOperationResponse);
                    productionTrackingResponse.setUserOperation(userOperationResponse);
                }

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

            response.setList(productionTrackingResponses).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;

    }

}
