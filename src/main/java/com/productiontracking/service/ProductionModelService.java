package com.productiontracking.service;

import com.productiontracking.dto.request.CreateProductionRequest;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.ProductionModel;

public interface ProductionModelService {
    ServiceResponse<ProductionModel> findAll();

    ServiceResponse<ProductionModel> create(CreateProductionRequest productionModel);

    ServiceResponse<ProductionModel> delete(Long id);

    ServiceResponse<ProductionModel> updateStatusById(Long id);

    ServiceResponse<ProductionModel> findAllByActive();

}
