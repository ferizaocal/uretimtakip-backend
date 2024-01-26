package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class UpdateRoleRequest {
    private Long id;
    private String name;
    private Long operationId;
}
