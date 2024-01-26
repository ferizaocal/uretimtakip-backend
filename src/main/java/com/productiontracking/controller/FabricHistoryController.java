package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateFabricHistoryRequest;
import com.productiontracking.dto.response.FabricHistoryResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.service.FabricHistoryService;

@RestController
@RequestMapping(value = "/api/v1")
public class FabricHistoryController {

    @Autowired
    private FabricHistoryService fabricHistoryService;

    @GetMapping(value = "/fabric-histories/{fabricId}")
    public ServiceResponse<FabricHistoryResponse> getFabricHistories(@PathVariable Long fabricId) {
        return fabricHistoryService.getFabricHistories(fabricId);
    }

    @PostMapping(value = "/fabric-history")
    public ServiceResponse<FabricHistoryResponse> addFabricHistoryEntry(
            @RequestBody CreateFabricHistoryRequest request) {
        return fabricHistoryService.addFabricHistoryEntry(request);
    }

    @DeleteMapping(value = "/fabric-history/{id}")
    public ServiceResponse<FabricHistoryResponse> deleteFabricHistoryEntry(@PathVariable Long id) {
        return fabricHistoryService.deleteFabricHistoryEntry(id);
    }
}
