package com.productiontracking.service;

import com.productiontracking.dto.request.CreateFabricRequest;
import com.productiontracking.dto.response.FabricFromBrandResponse;
import com.productiontracking.dto.response.FabricResponse;
import com.productiontracking.dto.response.ServiceResponse;

public interface FabricService {
    ServiceResponse<FabricResponse> createFabric(CreateFabricRequest request, Long userId);

    ServiceResponse<FabricResponse> delete(Long id);

    ServiceResponse<FabricResponse> findAll(Long userId);

    ServiceResponse<FabricFromBrandResponse> findAllFromFabricBrand(Long userId);
}
