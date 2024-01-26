package com.productiontracking.dto.response;

import java.util.Set;

import com.productiontracking.entity.Role;
import com.productiontracking.entity.Role.Status;

import lombok.Data;

@Data
public class LoginResponse {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String token;
    Status status;
    Set<Role> roles;
}
