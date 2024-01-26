package com.productiontracking.dto.request;

import lombok.Data;

@Data
public class UpdateCustomerRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String distrinct;
    private String country;
    private String postalCode;
}
