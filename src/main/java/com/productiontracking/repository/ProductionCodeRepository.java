package com.productiontracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.ProductionCode;

@Repository
public interface ProductionCodeRepository extends JpaRepository<ProductionCode, Long> {
    ProductionCode findByCode(String code);
}
