package com.productiontracking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.ProductionCodeHistory;
import com.productiontracking.repository.ProductionCodeHistoryRepository;

@Service
public class ProductionCodeHistoryImpl implements ProductionCodeHistoryService {

    @Autowired
    private ProductionCodeHistoryRepository productionCodeHistoryRepository;

    @Override
    public ServiceResponse<ProductionCodeHistory> findAll(Long productionCodeId) {
        ServiceResponse<ProductionCodeHistory> response = new ServiceResponse<>();
        try {
            List<ProductionCodeHistory> productionCodeHistoryList = productionCodeHistoryRepository
                    .findByProductionCodeId(productionCodeId);
            response.setList(productionCodeHistoryList).setIsSuccessful(true);

        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage().toString());
        }
        return response;
    }

}
