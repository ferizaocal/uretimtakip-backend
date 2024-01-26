package com.productiontracking.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.productiontracking.security.TokenProvider;

import io.jsonwebtoken.Claims;

@Component
public final class GetClaims {

    private static TokenProvider _jwtTokenProvider;

    @Autowired
    public void setJwtTokenProvider(TokenProvider jwtTokenProvider) {
        _jwtTokenProvider = jwtTokenProvider;
    }

    public static Long getUserIdFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring("bearer ".length());
        return Long.parseLong(_jwtTokenProvider.getClaimFromToken(token, Claims::getId));
    }

    public static String getUserRoleFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring("bearer ".length());
        return _jwtTokenProvider.getClaimFromToken(token, Claims::getSubject);
    }
}