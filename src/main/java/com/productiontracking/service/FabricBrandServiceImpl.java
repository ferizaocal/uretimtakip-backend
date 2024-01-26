package com.productiontracking.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateFabricBrandRequest;
import com.productiontracking.dto.request.UpdateFabricBrandRequest;
import com.productiontracking.dto.response.FabricBrandResponse;
import com.productiontracking.dto.response.FabricBrandWithModelResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.FabricBrand;
import com.productiontracking.entity.User;
import com.productiontracking.exception.NotFoundException;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.FabricBrandRepository;
import com.productiontracking.repository.FabricModelRepository;
import com.productiontracking.repository.UserRepository;

@Service
public class FabricBrandServiceImpl implements FabricBrandService {

    @Autowired
    ModelMapperService modelMapperService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FabricBrandRepository fabricBrandRepository;
    @Autowired
    private FabricModelRepository fabricModelRepository;

    @Override
    public ServiceResponse<FabricBrandResponse> create(CreateFabricBrandRequest request, Long createdBy) {
        ServiceResponse<FabricBrandResponse> response = new ServiceResponse<>();
        try {
            Optional<User> createdUser = userRepository.findById(createdBy);
            if (!createdUser.isPresent()) {
                new NotFoundException(String.format("User with id %s not found", createdBy));
            }
            FabricBrand fabricBrand = modelMapperService.forRequest().map(request, FabricBrand.class);
            fabricBrand.setProductionModelId(createdUser.get().getActiveProductionModelId());
            fabricBrandRepository.save(fabricBrand);
            FabricBrandResponse fabricBrandResponse = modelMapperService.forResponse().map(fabricBrand,
                    FabricBrandResponse.class);
            response.setEntity(fabricBrandResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricBrandResponse> update(UpdateFabricBrandRequest request) {
        ServiceResponse<FabricBrandResponse> response = new ServiceResponse<>();
        try {
            Optional<FabricBrand> fabricBrand = fabricBrandRepository.findById(request.getId());
            if (!fabricBrand.isPresent()) {
                new NotFoundException(String.format("FabricBrand with id %s not found", request.getId()));
            }
            fabricBrand.get().setName(request.getName());
            fabricBrandRepository.save(fabricBrand.get());
            FabricBrandResponse fabricBrandResponse = modelMapperService.forResponse().map(fabricBrand.get(),
                    FabricBrandResponse.class);
            response.setEntity(fabricBrandResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricBrandWithModelResponse> findById(Long id) {
        ServiceResponse<FabricBrandWithModelResponse> response = new ServiceResponse<>();
        try {
            Optional<FabricBrand> fabricBrand = fabricBrandRepository.findById(id);
            if (!fabricBrand.isPresent()) {
                new NotFoundException(String.format("FabricBrand with id %s not found", id));
            }
            FabricBrandWithModelResponse fabricBrandResponse = modelMapperService.forResponse().map(fabricBrand.get(),
                    FabricBrandWithModelResponse.class);
            response.setEntity(fabricBrandResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage())
                    .setHasExceptionError(true);
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricBrandWithModelResponse> findAll(Long userId) {
        ServiceResponse<FabricBrandWithModelResponse> response = new ServiceResponse<>();
        try {
            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent()) {
                new NotFoundException(String.format("User with id %s not found", userId));
            }

            List<FabricBrand> fabricBrands = fabricBrandRepository
                    .findAllByProductionModelId(user.get().getActiveProductionModelId());
            List<FabricBrandWithModelResponse> fabricBrandResponses = fabricBrands.stream()
                    .filter(x -> x.getIsDeleted() == false)
                    .map(fabricBrand -> modelMapperService.forResponse().map(fabricBrand,
                            FabricBrandWithModelResponse.class))
                    .collect(Collectors.toList());
            response.setList(fabricBrandResponses).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricBrandResponse> delete(Long id) {
        ServiceResponse<FabricBrandResponse> response = new ServiceResponse<>();
        try {
            Optional<FabricBrand> fabricBrand = fabricBrandRepository.findById(id);
            if (!fabricBrand.isPresent()) {
                new NotFoundException(String.format("FabricBrand with id %s not found", id));
            }
            if (fabricBrand.get().getFabricModels().size() > 0) {
                fabricBrand.get().getFabricModels().forEach(fabricModel -> {
                    fabricModel.setIsDeleted(true);
                    fabricModelRepository.save(fabricModel);
                });
            }
            fabricBrand.get().setIsDeleted(true);
            fabricBrandRepository.save(fabricBrand.get());
            FabricBrandResponse fabricBrandResponse = modelMapperService.forResponse().map(fabricBrand.get(),
                    FabricBrandResponse.class);
            response.setEntity(fabricBrandResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

}
