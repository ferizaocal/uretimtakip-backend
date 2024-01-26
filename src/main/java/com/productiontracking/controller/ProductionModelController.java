package com.productiontracking.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateProductionRequest;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.ProductionModel;
import com.productiontracking.service.ProductionModelService;

@RestController
@RequestMapping(value = "/api/v1")
public class ProductionModelController {

    private ProductionModelService productionModelService;

    public ProductionModelController(ProductionModelService productionModelService) {
        super();
        this.productionModelService = productionModelService;
    }

    @RequestMapping(value = "/production-model", method = RequestMethod.POST)
    public ServiceResponse<ProductionModel> create(@RequestBody CreateProductionRequest createProductionRequest) {
        return productionModelService.create(createProductionRequest);
    }

    @RequestMapping(value = "/production-model/{id}", method = RequestMethod.DELETE)
    public ServiceResponse<ProductionModel> delete(@PathVariable Long id) {
        return productionModelService.delete(id);
    }

    @RequestMapping(value = "/production-model/{id}", method = RequestMethod.PUT)
    public ServiceResponse<ProductionModel> updateStatusById(@PathVariable Long id) {
        return productionModelService.updateStatusById(id);
    }

    @RequestMapping(value = "/production-models", method = RequestMethod.GET)
    public ServiceResponse<ProductionModel> getAllProductionModels() {
        return productionModelService.findAll();
    }

    @RequestMapping(value = "/production-models/active", method = RequestMethod.GET)
    public ServiceResponse<ProductionModel> getAllProductionModelsByActive() {
        return productionModelService.findAllByActive();
    }
}
