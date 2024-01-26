package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "user_operations")
public class UserOperation extends BaseEntity {
    private Long userId;
    private Long operationId;
    private Long productionTrackingId;
    private Boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "operationId", referencedColumnName = "id", insertable = false, updatable = false)
    private Operation operation;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "productionTrackingId", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductionTracking productionTracking;

}
