package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateFabricRequest;
import com.productiontracking.dto.response.FabricFromBrandResponse;
import com.productiontracking.dto.response.FabricResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.service.FabricService;
import com.productiontracking.utils.GetClaims;

@RestController
@RequestMapping(value = "/api/v1")
public class FabricController {

    @Autowired
    private FabricService fabricService;

    @PostMapping(value = "/fabric")
    public ServiceResponse<FabricResponse> createFabric(@RequestBody CreateFabricRequest request,
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return fabricService.createFabric(request, userId);
    }

    @DeleteMapping(value = "/fabric/{id}")
    public ServiceResponse<FabricResponse> delete(@PathVariable Long id) {
        return fabricService.delete(id);
    }

    @GetMapping(value = "/fabrics")
    public ServiceResponse<FabricResponse> findAll(@RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return fabricService.findAll(userId);
    }

    @GetMapping(value = "/fabrics/from-model")
    public ServiceResponse<FabricFromBrandResponse> findAllFromFabricBrand(
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return fabricService.findAllFromFabricBrand(userId);
    }

}
