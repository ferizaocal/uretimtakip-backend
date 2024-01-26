package com.productiontracking.service;

import com.productiontracking.dto.request.CreateFabricHistoryRequest;
import com.productiontracking.dto.response.FabricHistoryResponse;
import com.productiontracking.dto.response.ServiceResponse;

public interface FabricHistoryService {
    ServiceResponse<FabricHistoryResponse> getFabricHistories(Long fabricId);

    ServiceResponse<FabricHistoryResponse> addFabricHistoryEntry(CreateFabricHistoryRequest request);

    ServiceResponse<FabricHistoryResponse> deleteFabricHistoryEntry(Long fabricHistoryId);
}
