package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "production_models")
public class ProductionModel extends BaseEntity {
    private String name;
    private String icon;
    private String status = Status.ACTIVE.toString();

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
