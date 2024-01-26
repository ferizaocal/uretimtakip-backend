package com.productiontracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.ProductionTrackingFabric;

@Repository
public interface ProductionTrackingFabricRepository extends JpaRepository<ProductionTrackingFabric, Long> {

}
