package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.CreateBankRequest;
import com.ayman.realestatetransactionsystem.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@RestController
public class BankAccountController {
    private final BankAccountService bankService;

    @Operation(
            summary = "Adds a bank account",
            description = "Users can only have one bank account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Bank account details required for account creation"
            )
    )
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBank(@RequestBody @Valid CreateBankRequest bankRequest){
        bankService.addBankAccount(bankRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Account added successfully"));
    }
}
