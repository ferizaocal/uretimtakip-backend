package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "fabric_brands")
public class FabricBrand extends BaseEntity {
    private Long productionModelId;
    private String name;

    @OneToMany(mappedBy = "fabricBrand")
    private List<FabricModel> fabricModels;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
