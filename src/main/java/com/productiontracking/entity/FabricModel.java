package com.productiontracking.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "fabric_models")
public class FabricModel extends BaseEntity {
    private Long fabricBrandId;
    private String name;

    @OneToOne
    @JoinColumn(name = "fabricBrandId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    private FabricBrand fabricBrand;

    @OneToMany(mappedBy = "fabricModel")
    private List<Fabric> fabrics;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
