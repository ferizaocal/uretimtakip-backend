package com.productiontracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.CustomerInfo;

@Repository
public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {
    CustomerInfo findByUserId(Long userId);
}
