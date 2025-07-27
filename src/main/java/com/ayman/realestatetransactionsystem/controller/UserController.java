package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.CreateLoginRequest;
import com.ayman.realestatetransactionsystem.model.dto.CreateUserRequest;
import com.ayman.realestatetransactionsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Register a user",
            description = "Creates a new user account with a unique username and email" +
                    "The user will be added to a keycloak realm" +
                    "The password will be securely stored using encryption" +
                    "User chooses role (Buyer, Seller, Broker) during registration.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "User details required for registration"
            )
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody CreateUserRequest userRequest){
        userService.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Registered successfully"));
    }

    @Operation(
            summary = "Returns a JWT token to a signed user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Login information required for JWT creation"
            )
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody CreateLoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Token: " + userService.login(loginRequest)));
    }
}
