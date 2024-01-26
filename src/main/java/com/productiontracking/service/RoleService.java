package com.productiontracking.service;

import com.productiontracking.dto.request.CreateRoleRequest;
import com.productiontracking.dto.request.UpdateRoleRequest;
import com.productiontracking.dto.response.RoleResponse;
import com.productiontracking.dto.response.ServiceResponse;

public interface RoleService {
    ServiceResponse<RoleResponse> createRole(CreateRoleRequest request);

    ServiceResponse<RoleResponse> updateRole(UpdateRoleRequest request);

    ServiceResponse<RoleResponse> deleteRole(Long id);

    ServiceResponse<RoleResponse> findAll(Long userId);

}
