package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class CreateOperationCompleteRequest {
    private Long productTrackingId;
    private Long userOperationId;
    private Long targetUserId;
}
