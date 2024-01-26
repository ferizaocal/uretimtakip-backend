package com.productiontracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateCustomerRequest;
import com.productiontracking.dto.request.UpdateCustomerRequest;
import com.productiontracking.dto.response.CustomerResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.service.CustomerService;

@RestController
@RequestMapping(value = "/api/v1")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(value = "/customer")
    public ServiceResponse<CustomerResponse> addCustomer(@RequestBody CreateCustomerRequest customerRequest) {
        return customerService.createCustomer(customerRequest);
    }

    @PutMapping(value = "/customer")
    public ServiceResponse<CustomerResponse> updateCustomer(@RequestBody UpdateCustomerRequest customerRequest) {
        return customerService.updateCustomer(customerRequest);
    }

    @GetMapping(value = "/customers")
    public ServiceResponse<CustomerResponse> getCustomers() {
        return customerService.getAllCustomer();
    }

}
