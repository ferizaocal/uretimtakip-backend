package com.productiontracking.entity;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "fabrics")
public class Fabric extends BaseEntity {
    private Long fabricModelId;
    private Long productionModelId;

    @OneToMany(mappedBy = "fabric")
    private List<FabricHistory> fabricHistories;

    @ManyToOne
    @JoinColumn(name = "fabricModelId", referencedColumnName = "id", insertable = false, updatable = false)
    private FabricModel fabricModel;

    public double getTotalQuantity() {
        double totalQuantity = 0;
        for (FabricHistory fabricHistory : fabricHistories.stream().filter(x -> x.getIsDeleted() == false)
                .collect(Collectors.toList())) {
            if (fabricHistory.getType().equals(FabricHistory.Type.IN.toString())) {
                totalQuantity += fabricHistory.getQuantity();
            } else {
                totalQuantity -= fabricHistory.getQuantity();
            }
        }
        return totalQuantity;
    }

}
