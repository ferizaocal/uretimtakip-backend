package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "production_code_histories")
public class ProductionCodeHistory extends BaseEntity {
    private Long productionCodeId;
    private Long productionId; // üretim id
    private String type;
    private int quantity;

    public enum Type {
        OUT,
        IN
    }
}
