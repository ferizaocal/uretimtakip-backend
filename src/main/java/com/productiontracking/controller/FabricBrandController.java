package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateFabricBrandRequest;
import com.productiontracking.dto.request.UpdateFabricBrandRequest;
import com.productiontracking.dto.response.FabricBrandResponse;
import com.productiontracking.dto.response.FabricBrandWithModelResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.service.FabricBrandService;
import com.productiontracking.utils.GetClaims;

@RestController
@RequestMapping(value = "/api/v1")
public class FabricBrandController {

    @Autowired
    private FabricBrandService fabricBrandService;

    @PostMapping(value = "/fabric-brand")
    public ServiceResponse<FabricBrandResponse> createFabricBrand(@RequestBody CreateFabricBrandRequest request,
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return fabricBrandService.create(request, userId);
    }

    @PutMapping(value = "/fabric-brand")
    public ServiceResponse<FabricBrandResponse> updateFabricBrand(@RequestBody UpdateFabricBrandRequest request) {
        return fabricBrandService.update(request);
    }

    @GetMapping(value = "/fabric-brands")
    public ServiceResponse<FabricBrandWithModelResponse> findAllFabricBrands(
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return fabricBrandService.findAll(userId);
    }

    @DeleteMapping(value = "/fabric-brand/{id}")
    public ServiceResponse<FabricBrandResponse> deleteFabricBrand(@PathVariable Long id) {
        return fabricBrandService.delete(id);
    }

}
