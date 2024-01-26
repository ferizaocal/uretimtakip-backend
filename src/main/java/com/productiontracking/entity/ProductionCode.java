package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "production_codes")
public class ProductionCode extends BaseEntity {
    Long productionModelId;
    String code;
}
