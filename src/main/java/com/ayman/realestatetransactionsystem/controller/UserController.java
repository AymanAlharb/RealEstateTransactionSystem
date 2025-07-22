package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.User;
import com.ayman.realestatetransactionsystem.model.dto.CreateUserRequest;
import com.ayman.realestatetransactionsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody CreateUserRequest userRequest){
        userService.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Registered successfully"));
    }

    @GetMapping("/hi")
    @PreAuthorize("hasRole('BROKER')")
    public ResponseEntity<ApiResponse> hi(){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Hi"));
    }
}
