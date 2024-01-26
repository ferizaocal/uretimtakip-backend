package com.productiontracking.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "fabric_histories")
public class FabricHistory extends BaseEntity {
    private Long fabricId;
    private String type;
    private Double quantity; // metre

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fabricId", insertable = false, updatable = false)
    private Fabric fabric;

    public enum Type {
        IN,
        OUT
    }
}
