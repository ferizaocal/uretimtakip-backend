package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "role_operations")
public class RoleOperation extends BaseEntity {
    private Long roleId;
    private Long operationId;

    @OneToOne
    @JoinColumn(name = "operationId", referencedColumnName = "id", insertable = false, updatable = false)
    private Operation operation;

    public RoleOperation() {
        super();
    }

    public RoleOperation(Long roleId, Long operationId) {
        super();
        this.roleId = roleId;
        this.operationId = operationId;
    }

    public String getOperationName() {
        return operation.getOperationName();
    }
}
