package com.productiontracking.dto.response;

import lombok.Data;

@Data
public class RoleResponse extends BaseResponse {
    private String name;
    private Long operationId;
    private String operationName;
}
