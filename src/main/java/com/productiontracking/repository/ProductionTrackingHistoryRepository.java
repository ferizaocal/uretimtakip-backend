package com.productiontracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.ProductionTrackingHistory;

@Repository
public interface ProductionTrackingHistoryRepository extends JpaRepository<ProductionTrackingHistory, Long> {
    ProductionTrackingHistory findByProductionTrackingIdAndOperationId(Long productTrackingId, Long operationId);
}
