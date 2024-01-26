package com.productiontracking.entity;

import java.util.Set;

import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "users")
@Entity
public class User extends BaseEntity {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String password;
        private String status = Status.ACTIVE.toString();
        private Long activeProductionModelId;
        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
        @JoinTable(name = "USER_ROLES", joinColumns = {
                        @JoinColumn(name = "USER_ID")
        }, inverseJoinColumns = {
                        @JoinColumn(name = "ROLE_ID") })
        Set<Role> roles;

        @JoinColumn(name = "id", referencedColumnName = "userId", insertable = false, updatable = false)
        @OneToOne(cascade = CascadeType.ALL)
        private CustomerInfo customerInfo;

        public enum Status {
                ACTIVE,
                INACTIVE
        }

        public User() {
                super();
        }
}
