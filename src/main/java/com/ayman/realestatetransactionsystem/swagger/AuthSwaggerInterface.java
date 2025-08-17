package com.ayman.realestatetransactionsystem.swagger;

import com.ayman.realestatetransactionsystem.model.dto.request.CreateLoginRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public interface AuthSwaggerInterface {

    @PostMapping
    @Operation(
            summary = "Register a user",
            description = "Creates a new user account with a unique username and email. " +
                    "The user will be added to a Keycloak realm. " +
                    "The password will be securely stored using encryption. " +
                    "User chooses role (Buyer, Seller, Broker) during registration.",
            requestBody = @RequestBody(
                    required = true,
                    description = "User details required for registration"
            )
    )
    ResponseEntity<?> register(CreateUserRequest requestDto);

    @PostMapping("/token")
    @Operation(
            summary = "Returns a JWT token to a signed-in user",
            requestBody = @RequestBody(
                    required = true,
                    description = "Login information required for JWT creation"
            )
    )
    ResponseEntity<?> login(CreateLoginRequest loginDto);
}
