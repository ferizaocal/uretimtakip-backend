package com.productiontracking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.UserOperation;

@Repository
public interface UserOperationRepository extends JpaRepository<UserOperation, Long> {
    List<UserOperation> findAllByUserId(Long userId);
}
