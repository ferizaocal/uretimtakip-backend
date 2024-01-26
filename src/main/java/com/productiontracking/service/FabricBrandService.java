package com.productiontracking.service;

import com.productiontracking.dto.request.CreateFabricBrandRequest;
import com.productiontracking.dto.request.UpdateFabricBrandRequest;
import com.productiontracking.dto.response.FabricBrandResponse;
import com.productiontracking.dto.response.FabricBrandWithModelResponse;
import com.productiontracking.dto.response.ServiceResponse;

public interface FabricBrandService {
    ServiceResponse<FabricBrandResponse> create(CreateFabricBrandRequest request, Long createdBy);

    ServiceResponse<FabricBrandResponse> update(UpdateFabricBrandRequest request);

    ServiceResponse<FabricBrandWithModelResponse> findById(Long id);

    ServiceResponse<FabricBrandWithModelResponse> findAll(Long userId);

    ServiceResponse<FabricBrandResponse> delete(Long id);

}
