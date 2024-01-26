package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class CreateFabricModelRequest {
    private Long fabricBrandId;
    private String name;
}
