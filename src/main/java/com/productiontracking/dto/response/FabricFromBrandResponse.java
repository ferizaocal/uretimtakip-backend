package com.productiontracking.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class FabricFromBrandResponse {
    private Long id;
    private String brandName;
    private String modelName;
    private List<FabricResponse> fabrics;
}
