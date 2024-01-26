package com.productiontracking.controller;

import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productiontracking.dto.request.LoginRequest;
import com.productiontracking.dto.response.LoginResponse;
import com.productiontracking.dto.response.ServiceResponse;
import com.productiontracking.security.TokenProvider;
import com.productiontracking.service.UserService;

@RestController
@RequestMapping(value = "/api/v1")
public class AuthController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);
    private UserService _userService;
    private AuthenticationManager _authenticationManager;
    private TokenProvider _jwtTokenProvider;

    public AuthController(UserService _pUserService,
            AuthenticationManager _pAuthenticationManager,
            TokenProvider _pJwtTokenProvider) {
        this._userService = _pUserService;
        this._authenticationManager = _pAuthenticationManager;
        this._jwtTokenProvider = _pJwtTokenProvider;

    }

    @PostMapping("/auth/login")
    public ServiceResponse<LoginResponse> forMobile(@RequestBody LoginRequest _pLoginDto) {
        logger.info("AuthController.forMobile()");
        return _userService.forMobilelogin(_pLoginDto, _authenticationManager, _jwtTokenProvider);
    }
}
