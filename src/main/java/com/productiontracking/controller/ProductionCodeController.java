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

import com.productiontracking.dto.request.ProductionCodeRequest;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.ProductionCode;
import com.productiontracking.entity.ProductionCodeHistory;
import com.productiontracking.service.ProductionCodeHistoryService;
import com.productiontracking.service.ProductionCodeService;
import com.productiontracking.utils.GetClaims;

@RestController
@RequestMapping(value = "/api/v1")
public class ProductionCodeController {

    @Autowired
    private ProductionCodeService productionCodeService;

    @Autowired
    private ProductionCodeHistoryService productionCodeHistoryService;

    @PostMapping(value = "/production-code")
    public ServiceResponse<ProductionCode> addProductionCode(@RequestBody ProductionCodeRequest productionCode,
            @RequestHeader("Authorization") String token) {
        Long createdBy = GetClaims.getUserIdFromToken(token);
        return productionCodeService.create(productionCode, createdBy);
    }

    @PutMapping(value = "/production-code/{id}")
    public ServiceResponse<ProductionCode> updateProductionCode(@RequestBody ProductionCodeRequest productionCode,
            @PathVariable Long id) {

        return productionCodeService.update(productionCode, id);
    }

    @DeleteMapping(value = "/production-code/{id}")
    public ServiceResponse<ProductionCode> deleteProductionCode(@PathVariable Long id) {
        return productionCodeService.delete(id);
    }

    @GetMapping(value = "/production-codes")
    public ServiceResponse<ProductionCode> getAllProductionCode(@RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return productionCodeService.getAll(userId);
    }

    @GetMapping(value = "/production-code/history/{id}")
    public ServiceResponse<ProductionCodeHistory> getProductionCodeHistories(@PathVariable Long id) {
        return productionCodeHistoryService.findAll(id);
    }

}
