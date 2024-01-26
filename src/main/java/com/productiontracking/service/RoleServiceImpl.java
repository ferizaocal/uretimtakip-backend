package com.productiontracking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productiontracking.dto.request.CreateRoleRequest;
import com.productiontracking.dto.request.UpdateRoleRequest;
import com.productiontracking.dto.response.RoleResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.entity.Operation;
import com.productiontracking.entity.Role;
import com.productiontracking.entity.RoleOperation;
import com.productiontracking.entity.User;
import com.productiontracking.exception.DuplicateRoleException;
import com.productiontracking.exception.NotFoundException;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.repository.OperationRepository;
import com.productiontracking.repository.RoleOperationRepository;
import com.productiontracking.repository.RoleRepository;
import com.productiontracking.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    ModelMapperService modelMapperService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private RoleOperationRepository roleOperationRepository;
    private OperationRepository operationRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleOperationRepository roleOperationRepository,
            OperationRepository operationRepository, UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleOperationRepository = roleOperationRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    public ServiceResponse<RoleResponse> createRole(CreateRoleRequest request) {
        ServiceResponse<RoleResponse> response = new ServiceResponse<>();
        try {
            Role exitsRole = roleRepository.findByNameAndIsDeleted(request.getName(), false);
            if (exitsRole != null) {
                throw new DuplicateRoleException(request.getName());
            }
            Role role = modelMapperService.forRequest().map(request, Role.class);
            role = roleRepository.save(role);
            RoleOperation roleOperation = new RoleOperation(role.getId(), request.getOperationId());
            roleOperationRepository.save(roleOperation);
            RoleResponse roleResponse = modelMapperService.forResponse().map(role, RoleResponse.class);
            response.setEntity(roleResponse).setIsSuccessful(true);
        } catch (Exception e) {
            log.error("Error while creating role", e);
            response.setHasExceptionError(true).setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<RoleResponse> updateRole(UpdateRoleRequest request) {
        ServiceResponse<RoleResponse> response = new ServiceResponse<>();
        try {
            Role exitsRole = roleRepository.findByNameAndIsDeleted(request.getName(), false);
            if (exitsRole != null && !exitsRole.getId().equals(request.getId())) {
                throw new DuplicateRoleException(request.getName());
            }
            Role role = modelMapperService.forRequest().map(request, Role.class);
            role = roleRepository.save(role);
            Operation exitsOperation = operationRepository.findById(request.getOperationId()).get();
            if (exitsOperation == null) {
                throw new NotFoundException("Not found operation name : " + request.getName() + "and operation id : "
                        + request.getOperationId());
            }
            RoleOperation roleOperation = roleOperationRepository.findByRoleId(role.getId());
            if (roleOperation != null) {

                roleOperation.setOperationId(request.getOperationId());
                roleOperationRepository.save(roleOperation);
            }
            RoleResponse roleResponse = modelMapperService.forResponse().map(role, RoleResponse.class);
            roleResponse.setOperationId(request.getOperationId());
            roleResponse.setOperationName(exitsOperation.getOperationName());
            response.setEntity(roleResponse).setIsSuccessful(true);
        } catch (Exception e) {
            log.error("Error while updating role", e);
            response.setHasExceptionError(true).setExceptionMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ServiceResponse<RoleResponse> deleteRole(Long id) {
        ServiceResponse<RoleResponse> response = new ServiceResponse<>();
        try {
            Role role = roleRepository.findById(id).get();
            role.setIsDeleted(true);
            roleRepository.save(role);
            RoleOperation roleOperation = roleOperationRepository.findByRoleId(id);
            roleOperation.setIsDeleted(true);
            roleOperationRepository.save(roleOperation);
            response.setIsSuccessful(true);
        } catch (Exception e) {
            log.error("Error while deleting role", e);
            response.setHasExceptionError(true).setExceptionMessage("Error while deleting role " + e.getMessage());
        }
        return response;
    }

    private Boolean isRoleExit(Role role, Long activeModelId) {
        Boolean isAdmin = role.getName().equals(Role.RoleName.Admin.toString());
        Boolean isCustomer = role.getName().equals(Role.RoleName.Customer.toString());
        return !isAdmin && !isCustomer;
    }

    @Override
    public ServiceResponse<RoleResponse> findAll(Long userId) {
        ServiceResponse<RoleResponse> response = new ServiceResponse<>();
        try {
            User admin = userRepository.findById(userId).get();
            List<Role> roles = roleRepository.findAll();
            if (roles.size() != 0) {
                List<RoleResponse> roleResponses = roles.stream()
                        .filter(x -> isRoleExit(x, admin.getActiveProductionModelId()))
                        .map(role -> {
                            RoleResponse roleResponse = modelMapperService.forResponse().map(role, RoleResponse.class);
                            RoleOperation roleOperation = roleOperationRepository.findByRoleId(role.getId());
                            if (roleOperation != null) {
                                roleResponse.setOperationId(roleOperation.getOperationId());
                                roleResponse.setOperationName(roleOperation.getOperationName());
                            }
                            return roleResponse;
                        }).collect(java.util.stream.Collectors.toList());
                response.setList(roleResponses).setIsSuccessful(true);
            }

        } catch (Exception e) {
            log.error("Error while getting all roles", e);
            response.setHasExceptionError(true).setExceptionMessage("Error while getting all roles " + e.getMessage());
        }
        return response;
    }
}
