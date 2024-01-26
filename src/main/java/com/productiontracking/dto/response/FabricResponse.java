package com.productiontracking.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class FabricResponse {
    Long id;
    String brandName;
    String fabricModel;
    Double totalQuantity;
    List<FabricHistoryResponse> fabricHistory;
}
