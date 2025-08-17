package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.constant.ApiRoutes;
import com.ayman.realestatetransactionsystem.model.dto.response.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateLoginRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateUserRequest;
import com.ayman.realestatetransactionsystem.model.dto.response.RegistrationResponse;
import com.ayman.realestatetransactionsystem.service.UserService;
import com.ayman.realestatetransactionsystem.swagger.AuthSwaggerInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping(ApiRoutes.AUTH)
@RestController
public class AuthController implements AuthSwaggerInterface {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody CreateUserRequest userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse> login(@RequestBody CreateLoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Token: " + userService.login(loginRequest)));
    }
}
