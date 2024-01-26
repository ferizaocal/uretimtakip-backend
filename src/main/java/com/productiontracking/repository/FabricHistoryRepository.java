package com.productiontracking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.FabricHistory;

@Repository
public interface FabricHistoryRepository extends JpaRepository<FabricHistory, Long> {
    List<FabricHistory> findByFabricId(Long fabricId);
}
