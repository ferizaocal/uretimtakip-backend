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

import com.productiontracking.dto.request.CreateUserRequest;
import com.productiontracking.dto.request.UpdateUserRequest;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.dto.response.UserResponse;
import com.productiontracking.model.UserOperationRole;
import com.productiontracking.service.UserService;
import com.productiontracking.utils.GetClaims;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @GetMapping(value = "/user/me")
    public ServiceResponse<UserResponse> me(@RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return userService.me(userId);
    }

    @PutMapping(value = "/user")
    public ServiceResponse<UserResponse> update(@RequestBody UpdateUserRequest createUserRequest,
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        createUserRequest.setId(userId);
        return userService.update(createUserRequest);
    }

    @PostMapping(value = "/user")
    public ServiceResponse<UserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return userService.create(createUserRequest);
    }

    @PutMapping(value = "/user/active-productionmodel/{id}")
    public ServiceResponse<UserResponse> updateActiveProductionModelById(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return userService.updateActiveProductionModelId(userId, id);
    }

    @GetMapping(value = "/user/role/{roleId}")
    public ServiceResponse<UserResponse> getUserByRoleId(@PathVariable Long roleId,
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return userService.getFindAllByUserRole(roleId, userId);
    }

    @GetMapping(value = "/user/operation-number/{operationNumber}")
    public ServiceResponse<UserOperationRole> getUserByOperationNumber(@PathVariable Long operationNumber,
            @RequestHeader("Authorization") String token) {
        Long userId = GetClaims.getUserIdFromToken(token);
        return userService.getFindAllByUserOperationNumber(userId, operationNumber);
    }

    @GetMapping(value = "/user/operation-number/{operationNumber}/production-model/{productionModelId}")
    public ServiceResponse<UserOperationRole> getUserByOperationNumberAndProductionModelId(
            @PathVariable Long operationNumber,
            @PathVariable Long productionModelId) {
        return userService.getFindAllByUserOperationNumberAndProductionModelId(operationNumber, productionModelId);
    }

    @DeleteMapping(value = "/user/{userId}")
    public ServiceResponse<UserResponse> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

}
