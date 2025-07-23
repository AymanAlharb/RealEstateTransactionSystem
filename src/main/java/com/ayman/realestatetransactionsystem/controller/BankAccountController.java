package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.CreateBankRequest;
import com.ayman.realestatetransactionsystem.service.BankAccountService;
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

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBank(@RequestBody @Valid CreateBankRequest bankRequest){
        bankService.addBankAccount(bankRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Account added successfully"));
    }
}
