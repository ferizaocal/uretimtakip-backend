package com.productiontracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productiontracking.entity.ProductionModel;
import java.util.List;

@Repository
public interface ProductionModelRepository extends JpaRepository<ProductionModel, Long> {

    ProductionModel findByNameAndIsDeleted(String name, Boolean isDeleted);

    List<ProductionModel> findAllByStatus(String status);
}
