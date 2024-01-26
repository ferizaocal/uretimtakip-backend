package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class CreateRoleRequest {
    private String name;
    private Long operationId;
}
