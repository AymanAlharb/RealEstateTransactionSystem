package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotEmpty(message = "The username can not be empty.")
    @Min(value = 4, message = "The username can not be shorter than 4 characters")
    @Min(value = 20, message = "The username can not be longer than 20 characters")
    @Column(columnDefinition = "varchar(20) not null unique")
    private String username;

    // TODO : Add password validation.
    @NotEmpty(message = "The password can not be empty.")
    @Column(columnDefinition = "varchar(256) not null")
    private String password;

    @Email
    @NotEmpty(message = "The email can not be empty.")
    @Column(columnDefinition = "varchar(320) not null unique")
    private String email;

    @NotEmpty(message = "The phone number can not be empty.")
    @Column(columnDefinition = "varchar(16) not null unique")
    private String phoneNumber;

    @NotEmpty(message = "The role can not be empty.")
    @Column(columnDefinition = "varchar(8) not null")
    @Pattern(regexp = "(?i)buyer|seller|broker")
    private String role;
}
