package com.productiontracking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateFabricRequest;
import com.productiontracking.dto.response.FabricFromBrandResponse;
import com.productiontracking.dto.response.FabricResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.Fabric;
import com.productiontracking.entity.FabricBrand;
import com.productiontracking.entity.FabricModel;
import com.productiontracking.entity.User;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.FabricBrandRepository;
import com.productiontracking.repository.FabricHistoryRepository;
import com.productiontracking.repository.FabricModelRepository;
import com.productiontracking.repository.FabricRepository;
import com.productiontracking.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FabricServiceImpl implements FabricService {

    @Autowired
    ModelMapperService modelMapperService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FabricRepository fabricRepository;

    @Autowired
    private FabricBrandRepository fabricBrandRepository;

    @Autowired
    private FabricHistoryRepository fabricHistoryRepository;

    @Override
    public ServiceResponse<FabricResponse> createFabric(CreateFabricRequest request, Long userId) {
        ServiceResponse<FabricResponse> response = new ServiceResponse<>();
        try {
            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent()) {
                log.error("User not found");
            }
            List<Fabric> isExits = fabricRepository.findByFabricModelIdAndProductionModelIdAndIsDeleted(
                    request.getFabricModelId(),
                    user.get().getActiveProductionModelId(), false);
            if (isExits != null && isExits.size() > 0) {
                log.error("Fabric already exists");
                throw new Exception("Kumaş zaten mevcut");
            }
            Fabric fabric = new Fabric();
            fabric.setFabricModelId(request.getFabricModelId());
            fabric.setProductionModelId(user.get().getActiveProductionModelId());
            fabricRepository.save(fabric);

            response.setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricResponse> delete(Long id) {
        ServiceResponse<FabricResponse> response = new ServiceResponse<>();
        try {
            Fabric fabric = fabricRepository.findById(id).get();
            if (fabric.getFabricHistories() != null && fabric.getFabricHistories().size() > 0) {
                fabricHistoryRepository.saveAll(fabric.getFabricHistories().stream().map(x -> {
                    x.setIsDeleted(true);
                    return x;
                }).collect(Collectors.toList()));
            }
            fabric.setIsDeleted(true);
            fabricRepository.save(fabric);
            response.setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricResponse> findAll(Long userId) {
        ServiceResponse<FabricResponse> response = new ServiceResponse<>();
        try {
            List<Fabric> fabrics = fabricRepository.findAll().stream().filter(x -> x.getIsDeleted() == false)
                    .collect(Collectors.toList());
            List<FabricResponse> fabricResponses = fabrics.stream().map(x -> {
                final String modelName = x.getFabricModel().getName();
                final String brandName = x.getFabricModel().getFabricBrand().getName();
                final Long fabricId = x.getId();
                x.setFabricModel(null);

                FabricResponse fabricResponse = modelMapperService.forResponse().map(x, FabricResponse.class);
                fabricResponse.setBrandName(brandName);
                fabricResponse.setFabricModel(modelName);
                fabricResponse.setId(fabricId);
                fabricResponse.setTotalQuantity(x.getTotalQuantity());
                return fabricResponse;
            }).collect(Collectors.toList());
            response.setList(fabricResponses).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricFromBrandResponse> findAllFromFabricBrand(Long userId) {
        ServiceResponse<FabricFromBrandResponse> response = new ServiceResponse<>();
        try {
            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent()) {
                log.error("User not found");
            }
            List<FabricBrand> fabricModels = fabricBrandRepository.findAll().stream()
                    .filter(x -> x.getIsDeleted() == false
                            && x.getProductionModelId().equals(user.get().getActiveProductionModelId()))
                    .collect(Collectors.toList());
            List<FabricFromBrandResponse> fabricFromModelResponses = fabricModels.stream().map(x -> {
                FabricFromBrandResponse fabricFromModelResponse = modelMapperService.forResponse().map(x,
                        FabricFromBrandResponse.class);
                List<Fabric> fabrics = new ArrayList<>();
                if (x.getFabricModels() != null && x.getFabricModels().size() > 0) {
                    x.getFabricModels().stream().filter(fm -> fm.getIsDeleted() == false).forEach(fm -> {
                        fabrics.addAll(fm.getFabrics().stream().filter(f -> f.getIsDeleted() == false)
                                .collect(Collectors.toList()));
                    });
                }
                List<FabricResponse> fabricResponses = fabrics.stream().map(y -> {
                    final String modelName = y.getFabricModel().getName();
                    final String brandName = y.getFabricModel().getFabricBrand().getName();
                    final Long fabricId = y.getId();
                    y.setFabricModel(null);
                    FabricResponse fabricResponse = modelMapperService.forResponse().map(y, FabricResponse.class);
                    fabricResponse.setBrandName(brandName);
                    fabricResponse.setFabricModel(modelName);
                    fabricResponse.setId(fabricId);
                    fabricResponse.setTotalQuantity(y.getTotalQuantity());
                    return fabricResponse;
                }).collect(Collectors.toList());
                fabricFromModelResponse.setFabrics(fabricResponses);
                return fabricFromModelResponse;
            }).collect(Collectors.toList());
            response.setList(fabricFromModelResponses).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

}
