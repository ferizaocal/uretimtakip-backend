package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "operations")
public class Operation extends BaseEntity {
    private Long productionModelId;
    private int operationNumber;
    private String operationName;
    private String status = Status.ACTIVE.toString();

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
