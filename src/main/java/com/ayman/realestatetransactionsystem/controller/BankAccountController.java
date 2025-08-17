package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.constant.ApiRoutes;
import com.ayman.realestatetransactionsystem.model.dto.response.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateBankRequest;
import com.ayman.realestatetransactionsystem.model.dto.response.BankAccountCreatedResponse;
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
@RequestMapping(ApiRoutes.BANK_ACCOUNT)
@RestController
public class BankAccountController {
    private final BankAccountService bankService;

    @PostMapping
    public ResponseEntity<BankAccountCreatedResponse> addBank(@RequestBody @Valid CreateBankRequest bankRequest){
        return ResponseEntity.status(HttpStatus.OK).body(bankService.addBankAccount(bankRequest));
    }
}
