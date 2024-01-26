package com.productiontracking.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class FabricHistoryResponse {
    private Long id;
    private String type;
    private Double quantity;
    private Date createdDate;
}
