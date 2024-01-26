package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class UpdateOperationRequest {
    private Long id;
    private int operationNumber;
    private String operationName;
    private String status;
}
