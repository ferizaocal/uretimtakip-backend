package com.productiontracking.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "production_tracking_histories")
@Data
public class ProductionTrackingHistory extends BaseEntity {
    private Long productionTrackingId;
    private Long operationId;
    private Date completionDate;

    @OneToOne
    @JoinColumn(name = "operationId", referencedColumnName = "id", insertable = false, updatable = false)
    private Operation operation;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "productionTrackingId", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductionTracking productionTracking;
}
