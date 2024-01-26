package com.productiontracking.dto.response;

import java.util.Date;
import java.util.List;

import com.productiontracking.entity.AgeGroup;
import com.productiontracking.entity.Operation;
import com.productiontracking.entity.ProductionCode;
import com.productiontracking.entity.ProductionModel;

import lombok.Data;

@Data
public class ProductionTrackingResponse {
    private Long id;
    private Long operationId;
    private int partyNumber;
    private String description;

    private Double totalMetre = 0.0;
    private int totalQuantity = 0;

    private AgeGroup ageGroup;
    private ProductionModel productionModel;
    private ProductionCode productionCode;
    private Operation operation;
    private Date createdDate;

    private UserOperationResponse userOperation;

    private List<UserOperationResponse> userOperations;
    private List<ProductionTrackingFabricResponse> productionTrackingFabrics;
    private List<ProductionTrackingHistoryResponse> productionTrackingHistories;
}
