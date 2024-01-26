package com.productiontracking.service;

import com.productiontracking.dto.request.ProductionCodeRequest;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.ProductionCode;

public interface ProductionCodeService {
    ServiceResponse<ProductionCode> create(ProductionCodeRequest productionCode, Long createdBy);

    ServiceResponse<ProductionCode> update(ProductionCodeRequest productionCode, Long id);

    ServiceResponse<ProductionCode> delete(Long id);

    ServiceResponse<ProductionCode> getAll(Long userId);
}
