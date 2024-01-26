package com.productiontracking.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private String status;

    @OneToMany(mappedBy = "order")
    public List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "customerId")
    public User customer;

    @OneToOne
    @JoinColumn(name = "productionCodeId")
    public ProductionCode productionCode;

    public enum Status {
        WAITING, COMPLETED
    }
}
