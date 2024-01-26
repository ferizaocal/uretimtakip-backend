package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "production_tracking_fabrics")
public class ProductionTrackingFabric extends BaseEntity {
    private Long productionTrackingId;
    private Long fabricId;
    private int quantity;
    private double metre;

    @OneToOne
    @JoinColumn(name = "fabricId", referencedColumnName = "id", insertable = false, updatable = false)
    private Fabric fabric;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "productionTrackingId", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductionTracking productionTracking;

}
