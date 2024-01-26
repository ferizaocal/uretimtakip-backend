package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class CreateOperationRequest {
    private int operationNumber;
    private String operationName;
}
