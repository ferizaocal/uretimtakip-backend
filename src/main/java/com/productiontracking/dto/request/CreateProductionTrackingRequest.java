package com.productiontracking.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateProductionTrackingRequest {
    private Long productionCodeId;
    private Long ageGroupId;
    private Long userId;
    private int partyNumber;
    private String description;
    List<CreateProductionFabricRequest> fabrics;
}
