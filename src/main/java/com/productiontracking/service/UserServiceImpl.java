package com.productiontracking.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.productiontracking.dto.request.CreateUserRequest;
import com.productiontracking.dto.request.LoginRequest;
import com.productiontracking.dto.request.UpdateUserRequest;
import com.productiontracking.dto.response.LoginResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.dto.response.UserResponse;
import com.productiontracking.entity.ProductionModel;
import com.productiontracking.entity.Role;
import com.productiontracking.entity.User;
import com.productiontracking.exception.NotFoundException;
import com.productiontracking.mapper.ModelMapperService;
import com.productiontracking.model.UserOperationRole;
import com.productiontracking.model.UserRole;
import com.productiontracking.repository.ProductionModelRepository;
import com.productiontracking.repository.RoleRepository;
import com.productiontracking.repository.UserRepository;
import com.productiontracking.repository.UserRoleRepository;
import com.productiontracking.security.TokenProvider;
import com.productiontracking.utils.GetClaims;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
    private ModelMapperService _modelMapperService;

    private UserRepository _userRepository;
    private UserRoleRepository _userRoleRepository;
    private PasswordEncoder _passwordEncoder;
    private RoleRepository _roleRepository;
    private ProductionModelRepository _productionModelRepository;

    public UserServiceImpl(UserRepository _pUserRepository, ModelMapperService _pModelMapperService,
            PasswordEncoder _pPasswordEncoder, RoleRepository _pRoleRepository,
            ProductionModelRepository _pProductionModelRepository, UserRoleRepository _pUserRoleRepository) {
        _userRepository = _pUserRepository;
        _modelMapperService = _pModelMapperService;
        _passwordEncoder = _pPasswordEncoder;
        _roleRepository = _pRoleRepository;
        _productionModelRepository = _pProductionModelRepository;
        _userRoleRepository = _pUserRoleRepository;
    }

    @Override
    public ServiceResponse<LoginResponse> forMobilelogin(LoginRequest _pUser,
            AuthenticationManager _pAuthenticationManager,
            TokenProvider _pJwtTokenProvider) {
        ServiceResponse<LoginResponse> _vResponse = new ServiceResponse<LoginResponse>();

        UsernamePasswordAuthenticationToken _vAuthenticationToken = new UsernamePasswordAuthenticationToken(
                _pUser.getEmail(), _pUser.getPassword());
        Authentication _vAuthentication = _pAuthenticationManager.authenticate(_vAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(_vAuthentication);
        String _vJwt = _pJwtTokenProvider.generateToken(_vAuthentication);
        User _vUser = _userRepository.findByEmail(_pUser.getEmail());
        LoginResponse _vLoginResponse = _modelMapperService.forResponse().map(_vUser, LoginResponse.class);
        _vLoginResponse.setToken(_vJwt);
        _vResponse.setEntity(_vLoginResponse)
                .setIsSuccessful(true);
        return _vResponse;
    }

    @Override
    public ServiceResponse<UserResponse> create(CreateUserRequest _pUser) {
        ServiceResponse<UserResponse> _vResponse = new ServiceResponse<UserResponse>();
        try {
            User _vUser = _modelMapperService.forRequest().map(_pUser, User.class);
            Set<Role> _vRoles = new HashSet<>();
            Role _vRole = _roleRepository.findByName(_pUser.getRole());
            if (_vRole == null) {
                logger.info("Not found role: " + _pUser.getRole());
                throw new NotFoundException("Not Found Role: " + _pUser.getRole());
            }
            _vRoles.add(_vRole);
            _vUser.setPassword(_passwordEncoder.encode(_pUser.getPassword()));
            _vUser.setRoles(_vRoles);
            _vUser = _userRepository.save(_vUser);
            UserResponse _vUserResponse = _modelMapperService.forResponse().map(_vUser, UserResponse.class);
            _vResponse.setEntity(_vUserResponse)
                    .setIsSuccessful(true);
            logger.info("User created: " + _vUser.getEmail() + " with role: " + _pUser.getRole());
        } catch (Exception e) {
            logger.error(e.getMessage());
            _vResponse.setExceptionMessage(e.getMessage())
                    .setHasExceptionError(true);
        }
        return _vResponse;
    }

    @Override
    public Long count() {
        return _userRepository.count();
    }

    @Override
    public ServiceResponse<UserResponse> updateActiveProductionModelId(Long _pId, Long _pProductionModelId) {
        ServiceResponse<UserResponse> _vResponse = new ServiceResponse<UserResponse>();
        try {
            ProductionModel _vProductionModel = _productionModelRepository.findById(_pProductionModelId)
                    .orElseThrow(() -> new NotFoundException("Production model not found id:" + _pProductionModelId));

            User _vUser = _userRepository.findById(_pId)
                    .orElseThrow(() -> new NotFoundException("User not found id:" + _pId));
            _vUser.setActiveProductionModelId(_vProductionModel.getId());
            _vUser = _userRepository.save(_vUser);
            UserResponse _vUserResponse = _modelMapperService.forResponse().map(_vUser, UserResponse.class);
            _vResponse.setEntity(_vUserResponse)
                    .setIsSuccessful(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            _vResponse.setExceptionMessage(e.getMessage())
                    .setHasExceptionError(true);
        }
        return _vResponse;
    }

    @Override
    public ServiceResponse<UserResponse> getFindAllByUserRole(Long roleId, Long getByUserId) {
        ServiceResponse<UserResponse> _vResponse = new ServiceResponse<>();
        try {
            User _vUser = _userRepository.findById(getByUserId)
                    .orElseThrow(() -> new NotFoundException("User not found id:" + getByUserId));
            List<UserRole> users = _userRoleRepository.findByRoleIdAndProductionModelId(roleId,
                    _vUser.getActiveProductionModelId());
            List<UserResponse> _vUserResponse = users.stream()

                    .map(user -> _modelMapperService.forResponse().map(user, UserResponse.class))
                    .collect(Collectors.toList());
            _vResponse.setList(_vUserResponse).setIsSuccessful(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            _vResponse.setExceptionMessage(e.getMessage());

        }
        return _vResponse;
    }

    @Override
    public ServiceResponse<UserOperationRole> getFindAllByUserOperationNumber(Long userId, Long operationNumber) {
        ServiceResponse<UserOperationRole> _vResponse = new ServiceResponse<>();
        try {

            User _vUser = _userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found id:" + userId));

            List<UserOperationRole> users = _userRoleRepository.findByProductionModelIdAndOperationNumber(
                    _vUser.getActiveProductionModelId(),
                    operationNumber);

            _vResponse.setList(users).setIsSuccessful(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            _vResponse.setExceptionMessage(e.getMessage());
        }
        return _vResponse;
    }

    @Override
    public ServiceResponse<UserResponse> deleteUser(Long userId) {
        ServiceResponse<UserResponse> _vResponse = new ServiceResponse<>();
        try {
            User _vUser = _userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found id:" + userId));
            _vUser.setStatus(User.Status.INACTIVE.toString());
            _vUser.setIsDeleted(true);
            _vUser = _userRepository.save(_vUser);
            _vResponse.setIsSuccessful(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            _vResponse.setExceptionMessage(e.getMessage());
        }
        return _vResponse;
    }

    @Override
    public ServiceResponse<UserOperationRole> getFindAllByUserOperationNumberAndProductionModelId(Long operationNumber,
            Long productionModelId) {
        ServiceResponse<UserOperationRole> _vResponse = new ServiceResponse<>();
        try {

            List<UserOperationRole> users = _userRoleRepository.findByProductionModelIdAndOperationNumber(
                    productionModelId,
                    operationNumber);

            _vResponse.setList(users).setIsSuccessful(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            _vResponse.setExceptionMessage(e.getMessage());
        }
        return _vResponse;
    }

    @Override
    public ServiceResponse<UserResponse> me(Long userId) {
        ServiceResponse<UserResponse> _vResponse = new ServiceResponse<>();
        try {
            User _vUser = _userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found id:" + userId));
            UserResponse _vUserResponse = _modelMapperService.forResponse().map(_vUser, UserResponse.class);
            _vResponse.setEntity(_vUserResponse).setIsSuccessful(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            _vResponse.setExceptionMessage(e.getMessage());
        }
        return _vResponse;
    }

    @Override
    public ServiceResponse<UserResponse> update(UpdateUserRequest _pUser) {
        ServiceResponse<UserResponse> _vResponse = new ServiceResponse<UserResponse>();
        try {
            User _vUser = _userRepository.findById(_pUser.getId())
                    .orElseThrow(() -> new NotFoundException("User not found id:" + _pUser.getId()));
            _vUser.setFirstName(_pUser.getFirstName());
            _vUser.setLastName(_pUser.getLastName());
            _vUser = _userRepository.save(_vUser);
            UserResponse _vUserResponse = _modelMapperService.forResponse().map(_vUser, UserResponse.class);
            _vResponse.setEntity(_vUserResponse)
                    .setIsSuccessful(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            _vResponse.setExceptionMessage(e.getMessage())
                    .setHasExceptionError(true);
        }
        return _vResponse;
    }

}
