package com.ayman.realestatetransactionsystem.controller;

import com.ayman.realestatetransactionsystem.exception.ApiResponse;
import com.ayman.realestatetransactionsystem.model.dto.CreateApprovalRequest;
import com.ayman.realestatetransactionsystem.model.dto.PaymentRequest;
import com.ayman.realestatetransactionsystem.service.TransectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(
            summary = "Request a property purchase",
            description = "Only buyers can request properties" +
                    "This is the first endpoint of four endpoint to complete the transection" +
                    "Property gets locked during the request"
    )
    @PostMapping("/request/{propertyId}")
    public ResponseEntity<ApiResponse> requestProperty(@Parameter(
            description = "Property ID required for property request",
            required = true) @PathVariable Long propertyId) {
        transectionService.requestProperty(propertyId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Request made successfully"));
    }

    @Operation(
            summary = "Seller approve or disapprove a property transection request",
            description = "Only sellers who own the property can approve the transaction",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property id and decision required"
            )
    )
    @PatchMapping("/seller-process-transection")
    public ResponseEntity<ApiResponse> sellerApproveOrDissApprove(@RequestBody @Valid CreateApprovalRequest approvalRequest) {
        transectionService.sellerApproveOrDissApprove(approvalRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Process completed successfully"));
    }

    @Operation(
            summary = "Broker approve or disapprove a property transection request",
            description = "Only brokers who has authorities on the property can approve the transaction",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Property id and decision required"
            )
    )
    @PatchMapping("/broker-process-transection")
    public ResponseEntity<ApiResponse> brokerApproveOrDissApprove(@RequestBody @Valid CreateApprovalRequest approvalRequest) {
        transectionService.brokerApproveOrDissApprove(approvalRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Process completed successfully"));
    }

    @Operation(
            summary = "buyer paying the property price" +
                    "Last step of the property transection",
            description = "Only buyers who requested the transection can pay for the property",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Transection id and CVV required"
            )
    )
    @PostMapping("/payment")
    public ResponseEntity<ApiResponse> payment(@RequestBody @Valid PaymentRequest paymentRequest) {
        transectionService.payment(paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Payment successfully"));
    }

    @Operation(
            summary = "Broker canceling Transection",
            description = "Only brokers who has authorities on the property can cancel the transaction",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Transection id and CVV required"
            )
    )
    @PatchMapping("/cancel/{transectionId}")
    public ResponseEntity<ApiResponse> cancelTransection(@PathVariable Long transectionId) {
        transectionService.cancelTransection(transectionId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Transection canceled successfully"));
    }
}
