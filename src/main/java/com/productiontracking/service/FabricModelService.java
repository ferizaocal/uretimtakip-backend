package com.productiontracking.service;

import com.productiontracking.dto.request.CreateFabricModelRequest;
import com.productiontracking.dto.request.UpdateFabricModelRequest;
import com.productiontracking.dto.response.FabricModelResponse;
import com.productiontracking.dto.response.ServiceResponse;

public interface FabricModelService {
    ServiceResponse<FabricModelResponse> create(CreateFabricModelRequest request);

    ServiceResponse<FabricModelResponse> update(UpdateFabricModelRequest request);

    ServiceResponse<FabricModelResponse> delete(Long id);

    ServiceResponse<FabricModelResponse> findAllByBrandId(Long brandId);
}
