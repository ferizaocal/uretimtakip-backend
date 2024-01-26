package com.productiontracking.service;

import com.productiontracking.dto.request.CreateCustomerRequest;
import com.productiontracking.dto.request.UpdateCustomerRequest;
import com.productiontracking.dto.response.CustomerResponse;
import com.productiontracking.dto.response.ServiceResponse;

public interface CustomerService {
    ServiceResponse<CustomerResponse> createCustomer(CreateCustomerRequest request);

    ServiceResponse<CustomerResponse> updateCustomer(UpdateCustomerRequest request);

    ServiceResponse<CustomerResponse> getAllCustomer();
}
