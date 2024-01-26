package com.productiontracking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.Fabric;

@Repository
public interface FabricRepository extends JpaRepository<Fabric, Long> {

    List<Fabric> findByFabricModelIdAndProductionModelIdAndIsDeleted(Long fabricModelId, Long productionModelId,
            Boolean isDeleted);
}
