package com.productiontracking.dto.response;

import lombok.Data;

@Data
public class ProductionTrackingFabricResponse {
    private Long id;
    private Long fabricId;
    private String fabricName;
    private String fabricModel;
    private double quantity;
    private double metre;
}
