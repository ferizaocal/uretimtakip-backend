package com.productiontracking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productiontracking.entity.ProductionCodeHistory;

public interface ProductionCodeHistoryRepository extends JpaRepository<ProductionCodeHistory, Long> {
    List<ProductionCodeHistory> findByProductionCodeId(Long productionCodeId);
}
