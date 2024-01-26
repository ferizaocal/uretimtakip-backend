package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateFabricModelRequest;
import com.productiontracking.dto.request.UpdateFabricModelRequest;
import com.productiontracking.dto.response.FabricModelResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.service.FabricModelService;

@RestController
@RequestMapping(value = "/api/v1")
public class FabricModelController {

    @Autowired
    private FabricModelService fabricModelService;

    @PostMapping(value = "/fabric-model")
    public ServiceResponse<FabricModelResponse> createFabricModel(@RequestBody CreateFabricModelRequest request) {
        return fabricModelService.create(request);
    }

    @PutMapping(value = "/fabric-model")
    public ServiceResponse<FabricModelResponse> updateFabricModel(@RequestBody UpdateFabricModelRequest request) {
        return fabricModelService.update(request);
    }

    @GetMapping(value = "/fabric-models/{brandId}")
    public ServiceResponse<FabricModelResponse> findAllFabricModels(@PathVariable Long brandId) {
        return fabricModelService.findAllByBrandId(brandId);
    }

    @DeleteMapping(value = "/fabric-model/{id}")
    public ServiceResponse<FabricModelResponse> deleteFabricModel(@PathVariable Long id) {
        return fabricModelService.delete(id);
    }

}
