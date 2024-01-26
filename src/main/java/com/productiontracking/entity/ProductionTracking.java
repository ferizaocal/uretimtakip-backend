package com.productiontracking.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "production_trackings")
@Entity
public class ProductionTracking extends BaseEntity {
    @Column(name = "image", length = 100000)
    private byte[] image;
    private String fileName;
    private Long productionModelId;
    private Long productionCodeId;
    private Long ageGroupId;
    private Long operationId;
    private int partyNumber;
    private String description;

    @JoinColumn(name = "productionModelId", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private ProductionModel productionModel;

    @OneToOne
    @JoinColumn(name = "productionCodeId", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductionCode productionCode;

    @OneToOne
    @JoinColumn(name = "ageGroupId", referencedColumnName = "id", insertable = false, updatable = false)
    private AgeGroup ageGroup;

    @OneToOne
    @JoinColumn(name = "operationId", referencedColumnName = "id", insertable = false, updatable = false)
    private Operation operation;

    @OneToMany(mappedBy = "productionTracking", cascade = javax.persistence.CascadeType.ALL)
    private List<UserOperation> userOperations;

    @OneToMany(mappedBy = "productionTracking", cascade = javax.persistence.CascadeType.ALL)
    private List<ProductionTrackingFabric> productionTrackingFabrics;

    @OneToMany(mappedBy = "productionTracking", cascade = javax.persistence.CascadeType.ALL)
    private List<ProductionTrackingHistory> productionTrackingHistories;

}
