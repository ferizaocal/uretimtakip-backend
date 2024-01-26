package com.productiontracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.AgeGroup;

@Repository
public interface AgeGroupRepository extends JpaRepository<AgeGroup, Long> {

}
