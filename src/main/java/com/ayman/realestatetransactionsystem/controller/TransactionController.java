package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.constant.ApiRoutes;
import com.ayman.realestatetransactionsystem.model.dto.response.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateApprovalRequest;
import com.ayman.realestatetransactionsystem.model.dto.request.PaymentRequest;
import com.ayman.realestatetransactionsystem.model.dto.response.PropertyRequestResponse;
import com.ayman.realestatetransactionsystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(ApiRoutes.TRANSACTION)
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/{propertyId}/request")
    public ResponseEntity<PropertyRequestResponse> requestProperty(@PathVariable Long propertyId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.requestProperty(propertyId));
    }

    @PatchMapping("/seller-process")
    public ResponseEntity<PropertyRequestResponse> sellerApproveOrDissApprove(@RequestBody @Valid CreateApprovalRequest approvalRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.sellerApproveOrDissApprove(approvalRequest));
    }

    @PatchMapping("/broker-process")
    public ResponseEntity<PropertyRequestResponse> brokerApproveOrDissApprove(@RequestBody @Valid CreateApprovalRequest approvalRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.brokerApproveOrDissApprove(approvalRequest));
    }

    @PostMapping("/payment")
    public ResponseEntity<PropertyRequestResponse> payment(@RequestBody @Valid PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.payment(paymentRequest));
    }
}
