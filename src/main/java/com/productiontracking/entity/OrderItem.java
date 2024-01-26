package com.productiontracking.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    int quantity;
    String description;

    @ManyToOne
    @JoinColumn(name = "orderId")
    public Order order;

    @ManyToOne
    @JoinColumn(name = "ageGroupId")
    public AgeGroup ageGroup;
}
