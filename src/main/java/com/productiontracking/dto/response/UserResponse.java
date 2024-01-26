package com.productiontracking.dto.response;

import java.util.List;

import com.productiontracking.entity.Role;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String status;
    private List<Role> roles;
}
