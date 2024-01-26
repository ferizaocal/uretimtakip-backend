package com.productiontracking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.FabricBrand;

@Repository
public interface FabricBrandRepository extends JpaRepository<FabricBrand, Long> {
    List<FabricBrand> findAllByProductionModelId(Long productionModelId);
}
