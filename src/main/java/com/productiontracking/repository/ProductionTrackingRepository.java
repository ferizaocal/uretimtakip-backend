package com.productiontracking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.ProductionTracking;

@Repository
public interface ProductionTrackingRepository extends JpaRepository<ProductionTracking, Long> {
        ProductionTracking findByPartyNumberAndIsDeletedAndProductionModelId(int partyNumber, boolean b,
                        Long productionModelId);

        List<ProductionTracking> findByProductionModelIdAndOperationIdAndIsDeleted(
                        Long productionModelId, Long operationId, boolean b);

        List<ProductionTracking> findByProductionModelIdAndIsDeleted(Long productionModelId, boolean b);

}
