package com.ayman.realestatetransactionsystem.swagger;

import com.ayman.realestatetransactionsystem.model.dto.request.CreateBankRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public interface BankAccountSwaggerInterface {
    @PostMapping
    @Operation(
            summary = "Adds a bank account",
            description = "Users can only have one bank account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Bank account details required for account creation"
            )
    )
    ResponseEntity<?> addBank(CreateBankRequest bankRequest);
}
