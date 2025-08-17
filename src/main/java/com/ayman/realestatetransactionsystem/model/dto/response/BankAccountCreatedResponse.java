package com.ayman.realestatetransactionsystem.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class BankAccountCreatedResponse {
    private String username;
    private double balance;
    private String accountNumber;
    private LocalDateTime createdAt;
}
