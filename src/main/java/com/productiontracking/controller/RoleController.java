package com.productiontracking.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.CreateRoleRequest;
import com.productiontracking.dto.request.UpdateRoleRequest;
import com.productiontracking.dto.response.RoleResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.service.RoleService;
import com.productiontracking.utils.GetClaims;

@RestController
@RequestMapping(value = "/api/v1")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        super();
        this.roleService = roleService;
    }

    @PostMapping(value = "/role")
    public ServiceResponse<RoleResponse> createRole(@RequestBody CreateRoleRequest request) {
        return roleService.createRole(request);
    }

    @PutMapping(value = "/role")
    public ServiceResponse<RoleResponse> updateRole(@RequestBody UpdateRoleRequest request) {
        return roleService.updateRole(request);
    }

    @GetMapping(value = "/roles")
    public ServiceResponse<RoleResponse> getRoles(@RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return roleService.findAll(userId);
    }

    @DeleteMapping(value = "/role/{id}")
    public ServiceResponse<RoleResponse> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}
