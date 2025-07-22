package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotEmpty(message = "The username can not be empty.")
    @Size(min = 4, max = 20,
            message = "The username can not be shorter than 4 characters and not longer than 20 characters")
    private String username;

    // TODO : Uncomment
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//            message = "Password must be at least 8 characters and include uppercase, lowercase, digit, and special character."
//    )
    @NotEmpty(message = "The password can not be empty.")
    private String password;

    @Email
    @NotEmpty(message = "The email can not be empty.")
    private String email;

    @NotEmpty(message = "The phone number can not be empty.")
    private String phoneNumber;

    @NotEmpty(message = "The role can not be empty.")
    @Pattern(regexp = "^(?i)(BUYER|SELLER|BROKER)$",
            message = "Role must be buyer, seller, or broker.")
    private String role;
}
