package com.productiontracking.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateCustomerRequest;
import com.productiontracking.dto.request.UpdateCustomerRequest;
import com.productiontracking.dto.response.CustomerResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.CustomerInfo;
import com.productiontracking.entity.Role;
import com.productiontracking.entity.User;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.CustomerInfoRepository;
import com.productiontracking.repository.RoleRepository;
import com.productiontracking.repository.UserRepository;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    ModelMapperService modelMapperService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerInfoRepository customerInfoRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public ServiceResponse<CustomerResponse> createCustomer(CreateCustomerRequest request) {
        ServiceResponse<CustomerResponse> response = new ServiceResponse<>();
        try {
            User isExitsEmail = userRepository.findByEmail(request.getEmail());
            if (isExitsEmail != null) {
                throw new Exception("Email already exists");
            }

            CustomerResponse customerResponse = new CustomerResponse();
            User customer = modelMapperService.forRequest().map(request, User.class);

            Role role = roleRepository.findByName(Role.RoleName.Customer.toString());
            if (role == null) {
                Role _role = new Role(Role.RoleName.Customer.toString());
                role = roleRepository.save(_role);
            }
            Set<Role> _vRoles = new HashSet<>();
            _vRoles.add(role);
            customer.setRoles(_vRoles);
            customer = userRepository.save(customer);
            CustomerInfo customerInfo = modelMapperService.forRequest().map(request, CustomerInfo.class);
            customerInfo.setUserId(customer.getId());
            customerInfo = customerInfoRepository.save(customerInfo);
            customerResponse = modelMapperService.forResponse().map(customer, CustomerResponse.class);
            customerResponse = modelMapperService.forResponse().map(customerInfo, CustomerResponse.class);
            customerResponse.setEmail(customer.getEmail());
            customerResponse.setFirstName(customer.getFirstName());
            customerResponse.setLastName(customer.getLastName());
            customerResponse.setPhone(customer.getPhone());
            customerResponse.setId(customer.getId());

            response.setEntity(customerResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<CustomerResponse> getAllCustomer() {
        ServiceResponse<CustomerResponse> response = new ServiceResponse<>();
        try {
            List<User> users = userRepository.findAll();
            users = users.stream().filter(
                    x -> x.getRoles().stream().anyMatch(y -> y.getName().equals(Role.RoleName.Customer.toString()))
                            && x.getIsDeleted() == false)
                    .collect(Collectors.toList());
            List<CustomerResponse> customerResponses = users.stream().map(y -> {
                CustomerResponse customerResponse = modelMapperService.forResponse().map(y, CustomerResponse.class);
                CustomerInfo customerInfo = customerInfoRepository.findByUserId(y.getId());
                customerResponse = modelMapperService.forResponse().map(customerInfo, CustomerResponse.class);
                customerResponse.setEmail(y.getEmail());
                customerResponse.setFirstName(y.getFirstName());
                customerResponse.setLastName(y.getLastName());
                customerResponse.setPhone(y.getPhone());
                customerResponse.setId(y.getId());

                return customerResponse;
            }).collect(Collectors.toList());
            response.setList(customerResponses).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<CustomerResponse> updateCustomer(UpdateCustomerRequest request) {
        ServiceResponse<CustomerResponse> response = new ServiceResponse<>();
        try {
            User customer = userRepository.findById(request.getId()).get();
            if (customer == null) {
                throw new Exception("Customer not found");
            }
            CustomerInfo customerInfo = customerInfoRepository.findByUserId(customer.getId());
            if (customerInfo == null) {
                throw new Exception("Customer info not found");
            }
            final Long customerInfoId = customerInfo.getId();
            customerInfo = modelMapperService.forRequest().map(request, CustomerInfo.class);
            customerInfo.setUserId(customer.getId());
            customerInfo.setId(customerInfoId);
            customerInfo = customerInfoRepository.save(customerInfo);

            customer.setFirstName(request.getFirstName());
            customer.setLastName(request.getLastName());
            customer.setPhone(request.getPhone());
            customer.setEmail(request.getEmail());

            customer = userRepository.save(customer);
            CustomerResponse customerResponse = modelMapperService.forResponse().map(customer, CustomerResponse.class);
            customerResponse = modelMapperService.forResponse().map(customerInfo, CustomerResponse.class);
            customerResponse.setEmail(customer.getEmail());
            customerResponse.setFirstName(customer.getFirstName());
            customerResponse.setLastName(customer.getLastName());
            customerResponse.setPhone(customer.getPhone());
            customerResponse.setId(customer.getId());
            response.setEntity(customerResponse).setIsSuccessful(true);
        } catch (Exception e) {
            response.setExceptionMessage(e.getMessage());
        }
        return response;
    }

}
