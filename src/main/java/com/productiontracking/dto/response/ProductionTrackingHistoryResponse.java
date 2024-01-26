package com.productiontracking.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class ProductionTrackingHistoryResponse {
    private Long id;
    private Long productionTrackingId;
    private Long operationId;
    private String operationName;
    private int operationNumber;
    private Date completionDate;
    private Date createdDate;
}
