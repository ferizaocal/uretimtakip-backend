package com.productiontracking.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateFabricModelRequest;
import com.productiontracking.dto.request.UpdateFabricModelRequest;
import com.productiontracking.dto.response.FabricModelResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.FabricModel;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.FabricModelRepository;

@Service
public class FabricModelServiceImpl implements FabricModelService {

    @Autowired
    ModelMapperService modelMapperService;

    @Autowired
    FabricModelRepository fabricModelRepository;

    @Override
    public ServiceResponse<FabricModelResponse> create(CreateFabricModelRequest request) {
        ServiceResponse<FabricModelResponse> response = new ServiceResponse<>();
        try {
            FabricModel fabricModel = modelMapperService.forRequest().map(request, FabricModel.class);
            fabricModelRepository.save(fabricModel);
            FabricModelResponse fabricModelResponse = modelMapperService.forResponse().map(fabricModel,
                    FabricModelResponse.class);
            response.setEntity(fabricModelResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricModelResponse> update(UpdateFabricModelRequest request) {
        ServiceResponse<FabricModelResponse> response = new ServiceResponse<>();
        try {
            Optional<FabricModel> fabricModel = fabricModelRepository.findById(request.getId());
            if (!fabricModel.isPresent()) {
                response.setExceptionMessage(String.format("FabricModel with id %s not found", request.getId()));
            }
            fabricModel.get().setName(request.getName());
            fabricModelRepository.save(fabricModel.get());
            FabricModelResponse fabricModelResponse = modelMapperService.forResponse().map(fabricModel.get(),
                    FabricModelResponse.class);
            response.setEntity(fabricModelResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricModelResponse> delete(Long id) {
        ServiceResponse<FabricModelResponse> response = new ServiceResponse<>();
        try {
            Optional<FabricModel> fabricModel = fabricModelRepository.findById(id);
            if (!fabricModel.isPresent()) {
                response.setExceptionMessage(String.format("FabricModel with id %s not found", id));
            }
            fabricModel.get().setIsDeleted(true);
            fabricModelRepository.save(fabricModel.get());
            FabricModelResponse fabricModelResponse = modelMapperService.forResponse().map(fabricModel.get(),
                    FabricModelResponse.class);
            response.setEntity(fabricModelResponse).setIsSuccessful(true);

        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<FabricModelResponse> findAllByBrandId(Long brandId) {
        ServiceResponse<FabricModelResponse> response = new ServiceResponse<>();
        try {
            List<FabricModel> fabricModel = fabricModelRepository.findAllByFabricBrandId(brandId);
            if (fabricModel.isEmpty()) {
                response.setExceptionMessage(String.format("FabricModel with brandId %s not found", brandId));
            }
            List<FabricModelResponse> fabricModelResponse = fabricModel.stream().filter(x -> x.getIsDeleted() == false)
                    .map(fabric -> modelMapperService.forResponse().map(fabric, FabricModelResponse.class))
                    .collect(Collectors.toList());
            response.setList(fabricModelResponse).setIsSuccessful(true);

        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

}
