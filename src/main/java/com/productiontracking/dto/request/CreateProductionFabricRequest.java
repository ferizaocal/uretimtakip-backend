package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class CreateProductionFabricRequest {
    private Long fabricId;
    private int quantity;
    private double metre;
}
