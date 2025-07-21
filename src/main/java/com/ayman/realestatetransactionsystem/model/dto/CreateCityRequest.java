package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCityRequest {
    @NotEmpty(message = "The account number can not be empty")
    private String name;

    @NotEmpty(message = "The balance can not be empty")
    private double region;
}
