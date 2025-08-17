package com.ayman.realestatetransactionsystem.model.mapper;

import com.ayman.realestatetransactionsystem.model.dto.response.BankAccountCreatedResponse;
import com.ayman.realestatetransactionsystem.model.entity.BankAccount;

import java.time.LocalDateTime;

public class BankAccountMapper {
    public static BankAccountCreatedResponse createBankResponse(BankAccount bankAccount){
        return BankAccountCreatedResponse.builder()
                .username(bankAccount.getUser().getUsername())
                .accountNumber(bankAccount.getAccountNumber())
                .balance(bankAccount.getBalance())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
