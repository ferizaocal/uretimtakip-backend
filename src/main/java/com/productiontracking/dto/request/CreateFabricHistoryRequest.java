package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class CreateFabricHistoryRequest {
    private Long fabricId;
    private Double quantity;
}
