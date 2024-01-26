package com.productiontracking.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class FabricBrandWithModelResponse {
    private Long id;
    private String name;
    private List<FabricModelResponse> fabricModels;
}
