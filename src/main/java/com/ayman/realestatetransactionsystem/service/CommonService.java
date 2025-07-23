package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommonService {
    public String getUsernameFromToken(Authentication auth){
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw new ApiException("Token not available");
        }
        Jwt jwt = jwtAuth.getToken();
       return jwt.getClaim("preferred_username");

    }
}
