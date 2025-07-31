package com.ayman.realestatetransactionsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommonService {
    public String getUsernameFromToken(Authentication auth){
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not available");
        }
        Jwt jwt = jwtAuth.getToken();
        String username = jwt.getClaim("preferred_username");

        log.info("Username: {} extracted from the jwt token", username);

       return username;
    }
}
