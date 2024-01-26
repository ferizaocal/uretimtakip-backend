package com.productiontracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.RoleOperation;

@Repository
public interface RoleOperationRepository extends JpaRepository<RoleOperation, Long> {
    RoleOperation findByRoleId(Long roleId);
}
