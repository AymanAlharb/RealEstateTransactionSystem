package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreatePropertyRequest {

    @NotEmpty(message = "The title can not be empty")
    @Max(value = 20, message = "The title can not be longer than 20")
    private String title;

    @NotEmpty(message = "The description can not be empty")
    @Max(value = 256, message = "The description can not be longer than 256")
    private String description;

    @NotNull(message = "The price can not be null")
    @Positive(message = "The price must be a positive number")
    private double price;

    @NotEmpty(message = "The status can not be empty")
    @Pattern(regexp = "^(?i)(AVAILABLE|LOCKED|SOLD|HIDDEN)$")
    private String status;

    @NotEmpty(message = "The location can not be empty")
    private String location;
}
