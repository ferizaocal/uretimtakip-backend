package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "customer_info")
public class CustomerInfo extends BaseEntity {
    private Long userId;
    private String address;
    private String city;
    private String distrinct;
    private String country;
    private String postalCode;

    @JsonBackReference
    @OneToOne(mappedBy = "customerInfo")
    public User user;
}
