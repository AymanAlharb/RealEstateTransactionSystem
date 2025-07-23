package com.ayman.realestatetransactionsystem.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBankRequest {
    @NotEmpty(message = "The account number can not be empty")
    @Size(min = 16, max = 16, message = "The account number must be 16 characters long")
    private String accountNumber;

    @NotNull(message = "The balance can not be empty")
    @PositiveOrZero(message = "The balance can not be a negative number")
    private double balance;
}
