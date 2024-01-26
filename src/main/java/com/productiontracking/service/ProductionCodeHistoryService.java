package com.productiontracking.service;

import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.ProductionCodeHistory;

public interface ProductionCodeHistoryService {
    ServiceResponse<ProductionCodeHistory> findAll(Long productionCodeId);
}
