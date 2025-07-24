package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.CreateApprovalRequest;
import com.ayman.realestatetransactionsystem.model.dto.PaymentRequest;
import com.ayman.realestatetransactionsystem.service.TransectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/transection")
@RestController
public class TransectionController {
    private final TransectionService transectionService;

    @PostMapping("/request/{propertyId}")
    public ResponseEntity<ApiResponse> requestProperty(@PathVariable Long propertyId){
        transectionService.requestProperty(propertyId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Request made successfully"));
    }

    @PatchMapping("/seller-process-transection")
    public ResponseEntity<ApiResponse> sellerApproveOrDissApprove(@RequestBody @Valid CreateApprovalRequest approvalRequest){
        transectionService.sellerApproveOrDissApprove(approvalRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Process completed successfully"));
    }

    @PatchMapping("/broker-process-transection")
    public ResponseEntity<ApiResponse> brokerApproveOrDissApprove(@RequestBody @Valid CreateApprovalRequest approvalRequest){
        transectionService.brokerApproveOrDissApprove(approvalRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Process completed successfully"));
    }

    @PostMapping("/payment")
    public ResponseEntity<ApiResponse> payment(@RequestBody @Valid PaymentRequest paymentRequest){
        transectionService.payment(paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Payment successfully"));
    }

    @PatchMapping("/cancel/{transectionId}")
    public ResponseEntity<ApiResponse> cancelTransection(@PathVariable Long transectionId){
        transectionService.cancelTransection(transectionId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Transection canceled successfully"));
    }
}
