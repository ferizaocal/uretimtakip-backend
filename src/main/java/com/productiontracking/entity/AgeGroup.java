package com.productiontracking.entity;

import javax.persistence.Entity;

import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "age_group")
public class AgeGroup extends BaseEntity {
    private String age;
}
